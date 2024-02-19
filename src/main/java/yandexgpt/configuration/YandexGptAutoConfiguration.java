package yandexgpt.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import yandexgpt.api.YandexGptApi;
import yandexgpt.client.YandexGptChatClient;


@AutoConfiguration
@ConditionalOnClass(YandexGptApi.class)
@EnableConfigurationProperties(YandexGptProperties.class)
public class YandexGptAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public YandexGptApi openAiApi(YandexGptProperties properties) {
        return new YandexGptApi(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public YandexGptChatClient openAiChatClient(YandexGptApi yandexGptApi, YandexGptProperties properties) {
        return new YandexGptChatClient(yandexGptApi, properties);
    }

}
