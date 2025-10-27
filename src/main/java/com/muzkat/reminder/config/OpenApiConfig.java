package com.muzkat.reminder.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;


/**
 * Конфигурационный класс для настройки OpenAPI/Swagger для документации REST API сервиса Reminder.
 * Определяет основные метаданные API (название, описание, контактные данные),
 * а также настраивает схему безопасности с использованием JWT (Bearer-токенов)
 */
@Configuration
public class OpenApiConfig {

    /**
     * Наименование схемы безопасности, используемой для авторизации с помощью JWT
     */
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * Создаёт и настраивает объект {@link OpenAPI} для генерации Swagger-документации.
     * Устанавливает информацию об API, контактные данные и схему безопасности с поддержкой JWT
     * @return сконфигурированный объект {@link OpenAPI}
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reminder API")
                        .version("1.0.0")
                        .description("Сервис для управления напоминаниями и отправки уведомлений по Email и Telegram")
                )
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}