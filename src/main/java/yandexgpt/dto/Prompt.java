package yandexgpt.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import yandexgpt.constants.RoleConstant;

public class Prompt {

    private final List<ChatCompletionMessage> messages;

    public Prompt(String contents) {
        this(new ChatCompletionMessage(RoleConstant.user, contents));
    }

    public Prompt(ChatCompletionMessage message) {
        this(Collections.singletonList(message));
    }

    public Prompt(List<ChatCompletionMessage> messages) {
        this.messages = messages;
    }

    public List<ChatCompletionMessage> getMessages() {
        return this.messages;
    }

    @Override
    public String toString() {
        return "Prompt{" + "messages=" + messages + '}';
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
        Prompt prompt = (Prompt) o;
        return Objects.equals(messages, prompt.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages);
    }

}
