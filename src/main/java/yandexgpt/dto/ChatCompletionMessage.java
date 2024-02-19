package yandexgpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import yandexgpt.constants.RoleConstant;


/**
 * A message representing the context for the completion model.
 *
 * @param roleConstant Identifier of the message sender. Could be one of the {@link RoleConstant} types.
 * @param text         Textual content of the message.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatCompletionMessage(
        @JsonProperty("role") RoleConstant roleConstant,
        @JsonProperty("text") String text) {
}
