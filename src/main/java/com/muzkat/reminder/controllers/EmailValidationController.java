package com.muzkat.reminder.controllers;

import com.muzkat.reminder.service.EmailValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для валидации email-адресов.
 * <p>
 *      Использует {@link EmailValidationService} для проверки существования и корректности email
 *      через внешний сервис (Hunter.io)
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Tag(name = "Email Validation", description = "Проверка валидности email-адресов через внешний API")
public class EmailValidationController {

    /**
     * Поле сервис валидации email-адреса пользователя
     */
    private final EmailValidationService emailValidationService;


    /**
     * Метод проверяет валидность указанного email-адреса.
     * Делает запрос к внешнему API (Hunter.io) и возвращает результат проверки
     * @param email email-адрес пользовавателя для проверки
     * @return {@code true}, если адрес прошёл валидацию, и {@code false}, если не прошел валидацию
     */
    @GetMapping("/validate")
    @Operation(
            summary = "Проверка валидности email пользователя",
            description = "Проверяет, существует ли указанный email-адрес с помощью внешнего API (Hunter.io)"
    )
    public ResponseEntity<Boolean> validateEmail(@Parameter(description = "Email-адрес, который необходимо проверить")
                                                     @RequestParam String email) {
        System.out.println("Проверка email: " + email);
        boolean isValid = emailValidationService.isEmailValid(email);
        return ResponseEntity.ok(isValid);
    }
}
