package com.muzkat.reminder.service;

import com.muzkat.reminder.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для отправки сообщений в Telegram.
 * Использует Telegram Bot API и настройки, заданные в {@link TelegramProperties}.
 * Выполняет HTTP-запросы через {@link RestTemplate} для отправки текстовых сообщений
 * в указанный чат Telegram
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService {

    /**
     * Поле конфигурационные параметры бота: токен и chatId
     */
    private final TelegramProperties telegramProperties;


    /**
     * Поле HTTP-клиент для выполнения запросов к Telegram Bot API
     */
    private final RestTemplate restTemplate = new RestTemplate();


    /**
     * Метод отправляет текстовое сообщение в Telegram-чат.
     * Формирует и отправляет POST-запрос на endpoint Telegram Bot API /sendMessage.
     * В случае ошибки логирует сообщение на уровне ERROR
     * @param message текст сообщения
     */
    public void sendMessage(String message) {
        String url = "https://api.telegram.org/bot" + telegramProperties.getToken() + "/sendMessage";
        Map<String, Object> request = new HashMap<>();
        request.put("chat_id", telegramProperties.getChatId());
        request.put("text", message);
        request.put("parse_mode", "Markdown");

        try {
            restTemplate.postForObject(url, request, String.class);
            log.info("Сообщение успешно отправлено в Telegram: {}", message);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в Telegram: {}", e.getMessage());
        }
    }
}
