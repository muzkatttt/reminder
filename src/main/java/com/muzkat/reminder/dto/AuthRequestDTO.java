package com.muzkat.reminder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO-класс для передачи данных пользователя при аутентификации
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO-класс для передачи данных пользователя при аутентификации")
public class AuthRequestDTO {

    /**
     * Поле имя пользователя
     */
    @Email
    @NotBlank
    private String email;

    /**
     * Поле пароль
     */
    @NotBlank
    private String password;

}