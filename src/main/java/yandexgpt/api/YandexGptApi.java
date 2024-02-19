package yandexgpt.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yandexgpt.configuration.YandexGptProperties;
import yandexgpt.constants.StatusConstant;
import yandexgpt.dto.ChatCompletionRequest;
import yandexgpt.dto.ChatCompletionResponse;
import yandexgpt.exception.YandexGptApiException;


public class YandexGptApi {
    private static final String DATA_LOGGING_ENABLED = "x-data-logging-enabled";
    private static final Predicate<String> SSE_DONE_PREDICATE = Predicate.not(StatusConstant.ALTERNATIVE_STATUS_FINAL.name()::contains);

    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    private final WebClient webClient;


    public YandexGptApi(YandexGptProperties properties) {

        Assert.hasLength(properties.getApiKey(), "Property apiKey must not be empty");
        Assert.hasLength(properties.getBaseUrl(), "Property baseUrl must not be empty");

        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Consumer<HttpHeaders> jsonContentHeaders = headers -> {
            headers.add(HttpHeaders.AUTHORIZATION, "Api-Key ".concat(properties.getApiKey()));
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (!properties.isEnableLogging()) {
                headers.add(DATA_LOGGING_ENABLED, Boolean.FALSE.toString());
            }
        };

        var responseErrorHandler = getResponseErrorHandler();

        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeaders(jsonContentHeaders)
                .defaultStatusHandler(responseErrorHandler)
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeaders(jsonContentHeaders)
                .build();
    }

    /**
     * Creates a model response for the given chat conversation.
     *
     * @param chatRequest The chat completion request.
     * @return Entity response with {@link ChatCompletionResponse} as a body and HTTP status code and headers.
     */
    public ResponseEntity<ChatCompletionResponse> chatCompletionEntity(ChatCompletionRequest chatRequest) {

        Assert.notNull(chatRequest, "The request body can not be null.");
        Assert.isTrue(!chatRequest.completionOptions().stream(), "Request must set the stream property to false.");

        return this.restClient.post()
                .uri("/completion")
                .body(chatRequest)
                .retrieve()
                .toEntity(ChatCompletionResponse.class);
    }

    /**
     * Creates a streaming chat response for the given chat conversation.
     *
     * @param chatRequest The chat completion request. Must have the stream property set to true.
     * @return Returns a {@link Flux} stream from chat completion chunks.
     */
    public Flux<ChatCompletionResponse> chatCompletionStream(ChatCompletionRequest chatRequest) {

        Assert.notNull(chatRequest, "The request body can not be null.");
        Assert.isTrue(chatRequest.completionOptions().stream(), "Request must set the stream property to true.");

        return this.webClient.post()
                .uri("/completion")
                .body(Mono.just(chatRequest), ChatCompletionRequest.class)
                .retrieve()
                .bodyToFlux(String.class)
                .filter(SSE_DONE_PREDICATE)
                .map(content -> parseJson(content, ChatCompletionResponse.class));
    }

    private static ResponseErrorHandler getResponseErrorHandler() {
        return new ResponseErrorHandler() {

            @JsonInclude(Include.NON_NULL)
            record ResponseError(@JsonProperty("error") Error error) {

                @JsonInclude(Include.NON_NULL)
                record Error(
                        @JsonProperty("grpcCode") Integer grpcCode,
                        @JsonProperty("httpCode") Integer httpCode,
                        @JsonProperty("message") String message,
                        @JsonProperty("httpStatus") String httpStatus,
                        @JsonProperty("details") List<?> details) {
                }
            }

            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return response.getStatusCode().isError();
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().isError()) {
                    throw new YandexGptApiException(String.format("%s - %s", response.getStatusCode().value(),
                            new ObjectMapper().readValue(response.getBody(), ResponseError.class)));
                }
            }
        };
    }

    private <T> T parseJson(String json, Class<T> type) {
        try {
            return this.objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new YandexGptApiException("Failed to parse schema: " + json, e);
        }
    }
}
