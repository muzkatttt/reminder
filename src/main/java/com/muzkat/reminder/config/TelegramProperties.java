package com.muzkat.reminder.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Класс для настройки конфигурации параметров Telegram-бота.
 * <p>
 *     Используется для маппинга значений из файла конфигурации (application.yml)
 *     с префиксом <b>telegram.bot</b> в соответствующие поля этого класса.
 *     Позволяет централизованно управлять параметрами подключения к Telegram API
 * </p>
 * Пример конфигурации:
 * <pre>
 * telegram:
 *   token: your_bot_token
 *   chat-id: your_chat_id
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "telegram")
@Getter
@Setter
public class TelegramProperties {

    /**
     * Токен авторизации бота в Telegram API. Получен у @BotFather,
     * используется для отправки запросов к Telegram Bot API
     */
    private String token;

    /**
     * Идентификатор чата  в Telegram, на который отправляются уведомления.
     * Можно получить при первом сообщении пользователя в бот
     */
    private String chatId;

    /**
     * Базовый URL Telegram API
     */
    private String baseUrl;

    /**
     * Путь к боту Telegram, содержащий префикс /bot и далее токен (добавляется вручную в коде)
     * Пример: /bot
     */
    private String botPath;

    /**
     * Путь к методу отправки сообщения Telegram API
     * Пример: /sendMessage
     */
    private String sendMessagePath;
}
