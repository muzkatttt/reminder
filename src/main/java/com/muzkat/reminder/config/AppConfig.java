package com.muzkat.reminder.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурационный класс приложения.
 * <p>
 *     Определяет два бина {@link RestTemplate}:
 *     <b>internalRestTemplate</b> — используется для взаимодействия с внутренними сервисами,
 *     где требуется авторизация через JWT.
 *     <b>externalRestTemplate</b> — используется для обращения к внешним API (Telegram Bot API, Hunter.io),
 *     где авторизация не требуется или реализована отдельно
 * <p>
 * Каждый бин маркируется аннотацией {@link Qualifier}, чтобы избежать конфликтов при внедрении зависимостей
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate с автоматическим добавлением JWT-токена в заголовок Authorization.
     * Используется для внутренних сервисов, которым требуется авторизация
     * @param builder для создания экземпляра RestTemplate
     * @return RestTemplate с JWT-интерцептором
     */
    @Bean
    @Qualifier("internalRestTemplate")
    public RestTemplate internalRestTemplate(RestTemplateBuilder builder) {
        return builder
                .interceptors((request, body, execution) -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication != null && authentication.getCredentials() instanceof String) {
                        String jwt = (String) authentication.getCredentials();
                        request.getHeaders().setBearerAuth(jwt);
                    }
                    return execution.execute(request, body);
                })
                .build();
    }

    /**
     * RestTemplate без JWT-интерцептора — используется для внешних API,
     * где авторизация реализована отдельно, либо не требуется (Telegram Bot API, Hunter.io)
     * @return RestTemplate без авторизации
     */
    @Bean
    @Primary
    @Qualifier("externalRestTemplate")
    public RestTemplate externalRestTemplate(){
        return new RestTemplate();
    }
}