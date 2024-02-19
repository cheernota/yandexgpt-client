package yandexgpt.constants;

/**
 * Identifier of the message sender.
 */
public enum RoleConstant {
    /**
     * special role used to define the behaviour of the completion model
     */
    system,
    /**
     * a role used by the model to generate responses
     */
    assistant,
    /**
     * a role used by the user to describe requests to the model
     */
    user;
}
