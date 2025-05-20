package com.muzkat.reminder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

/**
 * DTO-класс для представления входящего обновления от telegram API.
 * Содержит информацию о полученном ботом сообщении.
 * Применяется для сериализации запроса в формате JSON, который поступает на webhook
 */
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramUpdateDTO {

    /**
     * Вложенное сообщение в Telegram
     */
    private Message message;

    /**
     * Вложенный класс, который представляет тело сообщения от пользователя.
     * Содержит текст сообщения и информацию о чате
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {

        /**
         * Поле вложенного класса, которое представляет чат, откуда отправлено сообщение
         */
        private Chat chat;

        /**
         * Поле текст сообщения, отправленное поьзователем
         */
        private String text;
    }

    /**
     * Вложенный класс, который содержит информацию о telegram-чате или пользователе.
     * Используется для получения данных о чате и пользователе: <b>chat_id</b>, <b>first_name</b>,
     * <b>last_name</b>, <b>username</b>
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Chat {

        /**
         * Поле уникальный идентификатор telegram-чата
         */
        private Long id;

        /**
         * Поле имя пользователя в telegram
         */
        private String first_name;

        /**
         * Поле фамилия пользователя в telegram
         */
        private String last_name;

        /**
         * поле Username пользователя в telegram
         */
        private String username;
    }
}
