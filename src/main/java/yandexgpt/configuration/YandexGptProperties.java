package yandexgpt.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("spring.ai.yandexgpt")
public class YandexGptProperties {

    private String folderId;
    private String apiKey;
    private String baseUrl = "https://llm.api.cloud.yandex.net/foundationModels/v1";
    private boolean enableLogging = false;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

}
