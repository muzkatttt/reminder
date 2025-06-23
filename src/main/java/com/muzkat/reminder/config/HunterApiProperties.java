package com.muzkat.reminder.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для подключения к внешнему сервису (Hunter.io)
 * для проверки валидности email пользователя
 */
@Configuration
@ConfigurationProperties(prefix = "hunter.api")
@Data
public class HunterApiProperties {

    /**
     * API-ключ для доступа к Hunter.io
     */
    String key;

    /**
     * Базовый URL для запроса к Hunter.io
     */
    String url;
}
