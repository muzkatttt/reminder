package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.AuthRequestDTO;
import com.muzkat.reminder.dto.AuthResponseDTO;
import com.muzkat.reminder.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * Контроллер для обработки запросов аутентификации и регистрации пользователей.
 * <p>
 *     Предоставляет конечные точки для входа в систему и регистрации новых пользователей.
 *     Использует {@link AuthService} для выполнения бизнес-логики аутентификации и регистрации.
 * </p>
 * Конечные точки:
 * <p> <b>POST /auth/login</b> — аутентификация пользователя с использованием email и пароля.
 * Возвращает JWT-токен при успешной аутентификации
 * </p>
 *   <b>POST /auth/register</b> — регистрация нового пользователя с предоставленным email и паролем.
 *   Возвращает сообщение об успешной регистрации
 * </p>
 */

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Обработка запросов аутентификации и регистрации пользователей")
public class AuthController {

    /**
     * Поле экземпляр AuthService
     */
    private final AuthService authService;


    /**
     * Выполняет аутентификацию пользователя по имени и паролю
     * @param request DTO с именем пользователя и паролем
     * @return JWT токен в случае успешной аутентификации
     */
    @PostMapping("/login")
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Принимает email и пароль, возвращает JWT-токен при успешной авторизации",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO с email и паролем пользователя",
                    required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная аутентификация, JWT-токен в ответе"),
                    @ApiResponse(responseCode = "401", description = "Некорректные учетные данные")
            }
    )
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }


    /**
     * Регистрирует нового пользователя
     * @param request DTO с email и паролем
     * @return сообщение об успешной регистрации
     */
    @PostMapping("/register")
    @Operation(
            summary = "Регистрация пользователя",
            description = "Создает нового пользователя. Email пользователя должен быть уникальным и валидным",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO с email и паролем пользователя",
                    required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Пользователь уже существует или email невалиден")
            }
    )
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequestDTO request) {
        authService.register(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }
}
