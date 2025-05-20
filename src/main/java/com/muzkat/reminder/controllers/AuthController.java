package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.AuthRequestDTO;
import com.muzkat.reminder.dto.AuthResponseDTO;
import com.muzkat.reminder.service.AuthService;
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
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request){
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }


    /**
     * Регистрирует нового пользователя
     * @param request DTO с email и паролем
     * @return сообщение об успешной регистрации
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequestDTO request) {
        authService.register(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }
}
