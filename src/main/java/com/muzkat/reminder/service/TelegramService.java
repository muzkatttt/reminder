package com.muzkat.reminder.service;

import com.muzkat.reminder.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для асинхронной отправки сообщений в Telegram.
 * Использует Telegram Bot API и параметры из {@link TelegramProperties}.
 * Выполняет HTTP-запросы через {@link RestTemplate} для отправки текстовых сообщений
 * в указанный чат Telegram
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService {

    /**
     * Конфигурационные параметры для обращения к Telegram Bot API:
     * включает токен, chatId и пути к методам API.
     */
    private final TelegramProperties telegramProperties;


    /**
     * Поле HTTP-клиент для выполнения запросов к Telegram Bot API
     */
    @Qualifier("externalRestTemplate")
    private final RestTemplate restTemplate;


    /**
     * Режим форматирования сообщений в Telegram в формате Markdown
     */
    private static final String PARSE_MODE = "Markdown";


    /**
     * Метод отправляет текстовое сообщение в Telegram-чат.
     * Формирует и отправляет POST-запрос на endpoint Telegram Bot API /sendMessage.
     * В случае ошибки логирует сообщение на уровне ERROR
     * @param message текст сообщения
     */
    @Async
    public void sendMessage(String message) {
        String url = telegramProperties.getBaseUrl()
                     + telegramProperties.getBotPath()
                     + telegramProperties.getToken()
                     + telegramProperties.getSendMessagePath();

        Map<String, Object> request = new HashMap<>();
        request.put("chat_id", telegramProperties.getChatId());
        request.put("text", message);
        request.put("parse_mode", PARSE_MODE);

        try {
            restTemplate.postForObject(url, request, String.class);
            log.info("Сообщение успешно отправлено в Telegram: {}", message);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в Telegram: {}", e.getMessage());
        }
    }
}

