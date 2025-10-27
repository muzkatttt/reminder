package com.muzkat.reminder.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO-класс для представления JSON-ответа от внешнего сервиса Hunter.io.
 * Используется для анализа достоверности email-адреса при регистрации пользователя.
 * Объект включает вложенный объект {@link HunterData}, содержит параметры валидации (оценка достоверности (score),
 * результат проверки по регулярному выражению, наличие MX-записей и другие)
 * <p>
 *     В документации к Hunter.io содержится пояснение к интерпретации поля score:
 *     80–100 — надёжный email (safe to use)
 *     65–79 — может быть рисковым (risky)
 *     менее 65 — скорее всего, недействительный или ненадёжный.
 * </p>
 * Поэтому в данном классе при проверке валидности email применено условие score >= 80
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Ответ от сервиса по проверке корректности email-адреса Hunter.io")
public class HunterResponseDTO {

    /**
     * Основные данные результата проверки email в сервисе Hunter.io
     */
    @JsonProperty("data")
    private HunterData data;

    /**
     * Условие проверки валидности email пользователя в соответствии с официальной документацией Hunter.io
     * score >= 80
     */
    private final static int SCORE_VALUE = 80;

    /**
     * Вложенный DTO-класс (содержит детальные параметры результата валидации email-адреса пользователя).
     * Каждый параметр отражает определённую характеристику или этап проверки
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HunterData {

        /**
         * Статус email-адреса ("valid", "invalid", "accept_all")
         */
        private String status;

        /**
         * Общий результат проверки (устаревшее поле, заменено на {@code status})
         */
        private String result;

        /**
         * Оценка достоверности email от 0 до 100
         */
        private int score;

        /**
         * Проверяемый email-адрес пользователя
         */
        private String email;

        /**
         * Признак, прошёл ли email проверку по регулярному выражению
         */
        private boolean regexp;

        /**
         * Признак, является ли email пользователя бессмысленным набором символов
         */
        private boolean gibberish;

        /**
         * Признак, является ли email одноразовым (disposable)
         */
        private boolean disposable;

        /**
         * Признак, является ли email web-почтой
         */
        private boolean webmail;

        /**
         * Признак наличия MX-записей у домена
         */
        private boolean mx_records;

        /**
         * Признак доступности SMTP-сервера
         */
        private boolean smtp_server;

        /**
         * Признак прохождения SMTP-проверки
         */
        private boolean smtp_check;

        /**
         * Признак, принимает ли домен все письма (catch-all)
         */
        private boolean accept_all;

        /**
         * Признак, заблокирован ли email-адрес
         */
        private boolean block;

        /**
         * Метод проверки валидности email-адреса на основе статуса и оценки
         * @return true если статус "valid" или score >= 70
         */
        public boolean isValid() {
            return "valid".equalsIgnoreCase(status) || score >= SCORE_VALUE;
        }
    }
}
