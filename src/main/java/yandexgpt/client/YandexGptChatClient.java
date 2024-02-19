package yandexgpt.client;

import java.time.Duration;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.Flux;
import yandexgpt.api.YandexGptApi;
import yandexgpt.configuration.YandexGptProperties;
import yandexgpt.constants.ModelConstant;
import yandexgpt.dto.ChatCompletionRequest;
import yandexgpt.dto.ChatCompletionRequest.CompletionOptions;
import yandexgpt.dto.ChatCompletionResponse;
import yandexgpt.dto.Prompt;
import yandexgpt.exception.YandexGptApiException;


public class YandexGptChatClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final RetryTemplate retryTemplate = RetryTemplate.builder()
            .maxAttempts(5)
            .retryOn(YandexGptApiException.class)
            .exponentialBackoff(Duration.ofMillis(2000L), 5, Duration.ofMillis(3 * 60000L))
            .build();

    private final YandexGptApi yandexGptApi;
    private final YandexGptProperties properties;


    public YandexGptChatClient(YandexGptApi yandexGptApi,
                               YandexGptProperties properties) {
        this.yandexGptApi = yandexGptApi;
        this.properties = properties;
    }

    public String generateByPrompt(String contents) {
        return generate(new Prompt(contents), null, new CompletionOptions()).result().alternatives()
                .stream()
                .map(alt -> alt.message().text())
                .findFirst()
                .orElse("");
    }

    public ChatCompletionResponse generate(Prompt prompt, String model, CompletionOptions options) {
        return this.retryTemplate.execute(ctx -> {
            ResponseEntity<ChatCompletionResponse> completionEntity = this.yandexGptApi
                    .chatCompletionEntity(new ChatCompletionRequest(makeModelUri(model), options, prompt.getMessages()));

            var chatCompletion = completionEntity.getBody();
            if (chatCompletion == null) {
                logger.warn("No chat completion returned for request: {}", prompt.getMessages());
                return new ChatCompletionResponse(new ChatCompletionResponse.Result(Collections.emptyList(), null, null));
            }

            return chatCompletion;
        });
    }

    public Flux<ChatCompletionResponse> generateStream(Prompt prompt, String model, CompletionOptions options) {

        return this.retryTemplate.execute(ctx -> this.yandexGptApi
                .chatCompletionStream(new ChatCompletionRequest(makeModelUri(model), options, prompt.getMessages())));

    }

    private String makeModelUri(String model) {
        return String.format("gpt://%s/%s/latest", properties.getFolderId(), ModelConstant.convert(model).getModelName());
    }

}
