# yandexgpt-client
This library is written in Java 17 and provides a synchronous Java client for [Yandex GPT completion API](https://cloud.yandex.ru/ru/docs/yandexgpt/api-ref/v1/TextGeneration/completion).

## 1.1 Prerequisites
Created an authorization api-key and folder ID for Yandex Cloud, [official doc](https://cloud.yandex.ru/ru/docs/iam/operations/api-key/create) or [here](https://habr.com/ru/articles/780008/).

## 1.2 Configuration and Spring Boot dependency
You can leverage the `yandexgpt-client` library, for this clone the GitHub repository

```git clone https://github.com/cheernota/yandexgpt-client```

build the library:

```mvn clean install```

and add it as a dependency to your _pom.xml_:
```xml
<dependency>
    <groupId>com.cheernota</groupId>
    <artifactId>yandexgpt-client</artifactId>
    <version>1.0.0</version>
</dependency>
```
or _build.gradle_

```implementation 'com.cheernota:yandexgpt-client:1.0.0'```

Then add API-key and folder-ID to your application properties file
```
spring.ai.yandexgpt.folder-id=j39shh ...
spring.ai.yandexgpt.api-key=jkSS82kHH ...
```

## 1.3 Use case
After configuring you can autowire bean [YandexGptChatClient](src/main/java/yandexgpt/client/YandexGptChatClient.java) and use its methods in your project like this:
```java
private final YandexGptChatClient chatClient;

    @Autowired
    public GptService(YandexGptChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    public String askSimplePrompt() {
        String prompt = "Привет, расскажи насколько суров климат Сибири.";
        return chatClient.generateByPrompt(prompt);
    }
```

## 1.4 Other properties

| Property  | Description                                                   | Default                                                |
| ------------- |---------------------------------------------------------------|--------------------------------------------------------|
| spring.ai.yandexgpt.base-url  | Base url.                                                     | `https://llm.api.cloud.yandex.net/foundationModels/v1` |
| spring.ai.yandexgpt.enable-logging  | Special header for managing the logging of your request data. | `false` |

You can also configure the LLM request parameters manually, read the javadoc to the [ChatCompletionRequest](src/main/java/yandexgpt/dto/ChatCompletionRequest.java).