package com.muzkat.reminder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO-класс для ответа при аутентификации пользователя
 * <p>
 *     Содержит JWT-токен, предоставляемый клиенту после успешной аутентификации.
 *     Этот токен используется для последующих запросов к защищённым ресурсам
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    /**
     * Поле JWT-токен, предоставляемый после успешной аутентификации пользователя
     */
    private String token;
}
