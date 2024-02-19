package yandexgpt.exception;

public class YandexGptApiException extends RuntimeException {

    public YandexGptApiException(String message) {
        super(message);
    }

    public YandexGptApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
