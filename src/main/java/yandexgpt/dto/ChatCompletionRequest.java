package yandexgpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


/**
 * A request dto for the service to generate text completion.
 *
 * @param modelUri          The identifier of the model to be used for completion generation.
 * @param completionOptions Configuration options for completion generation.
 * @param messages          A list of messages representing the context for the completion model.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatCompletionRequest(
        @JsonProperty("modelUri") String modelUri,
        @JsonProperty("completionOptions") CompletionOptions completionOptions,
        @JsonProperty("messages") List<ChatCompletionMessage> messages) {


    /**
     * Configuration options for completion generation.
     *
     * @param stream      Enables streaming of partially generated text.
     * @param temperature Affects creativity and randomness of responses. Should be a double number between 0 (inclusive) and 1 (inclusive).
     *                    Lower values produce more straightforward responses, while higher values lead to increased creativity and randomness.
     *                    Default temperature: 0.6
     * @param maxTokens   The limit on the number of tokens used for single completion generation. Must be greater than zero.
     *                    The maximum allowed parameter value may depend on the model used.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record CompletionOptions(
            @JsonProperty("stream") Boolean stream,
            @JsonProperty("temperature") Float temperature,
            @JsonProperty("max_tokens") Integer maxTokens) {

        /**
         * Default constructor: non-stream mode, medium temperature, 1000 tokens for answer.
         */
        public CompletionOptions() {
            this(false, 0.5f, 1000);
        }

        /**
         * Shortcut constructor with variable number of tokens and stream mode.
         */
        public CompletionOptions(Boolean stream, Integer maxTokens) {
            this(stream, 0.5f, maxTokens);
        }
    }
}
