package yandexgpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import yandexgpt.constants.StatusConstant;


/**
 * A response containing generated text completions
 *
 * @param result A response result.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatCompletionResponse(@JsonProperty("result") Result result) {

    /**
     * Response result.
     *
     * @param alternatives A list of generated completion alternatives.
     * @param usage        A set of statistics describing the number of content tokens used by the completion model.
     *                     An object representing the number of content tokens used by the completion model.
     * @param modelVersion Model version (changes with model releases).
     */
    public record Result(
            @JsonProperty("alternatives") List<Alternative> alternatives,
            @JsonProperty("usage") CompletionUsage usage,
            @JsonProperty("modelVersion") String modelVersion) {


        /**
         * Generated completion alternatives.
         *
         * @param message A message containing the content of the alternative.
         *                A message object representing a wrapper over the inputs and outputs of the completion model.
         * @param status  The generation status of the alternative. Could be one of the {@link StatusConstant} types.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record Alternative(
                @JsonProperty("message") ChatCompletionMessage message,
                @JsonProperty("status") StatusConstant status) {
        }


        /**
         * An object representing the number of content tokens used by the completion model.
         *
         * @param inputTextTokens  The number of tokens in the text parts of the model input.
         * @param completionTokens The total number of tokens in the generated completions.
         * @param totalTokens      The total number of tokens, including all input tokens and all generated tokens.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record CompletionUsage(
                @JsonProperty("inputTextTokens") String inputTextTokens,
                @JsonProperty("completionTokens") String completionTokens,
                @JsonProperty("totalTokens") String totalTokens) {
        }
    }
}
