package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.TelegramUpdateDTO;
import com.muzkat.reminder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Контроллер для обработки входящих сообщений от Telegram-бота.
 * Используется для автоматической регистрации пользователя и
 * сохранения его chat_id по указанному email
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/telegram")
@Slf4j
public class TelegramRegistrationController {

    /**
     * Поле экземпляр {@link UserRepository}
     */
    private final UserRepository userRepository;


    /**
     * Получение сообщения от Telegram-бота.
     * Метод извлекает email из текстового сообщения и сохраняет chat_id в базе данных
     * для пользователя с указанным email, если пользователь существует в базе данных.
     * <p>
     *     Пользователь должен отправить свой email боту в сообщении.
     * </p>
     * @param dto DTO, содержащий сообщение от Telegram
     * @return HTTP-ответ с результатом регистрации Telegram-аккаунта
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> registrationUserByTelegram(@RequestBody TelegramUpdateDTO dto) {
        TelegramUpdateDTO.Message message = dto.getMessage();
        if (message == null || message.getChat() == null || message.getText() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Некорректный запрос: отсутствует сообщение, чат или текст");
        }

        Long chatId = message.getChat().getId();
        String email = message.getText().trim();

        return userRepository.findByEmail(email)
                .map(user -> {
                    user.setTelegramChatId(chatId.toString());
                    userRepository.save(user);

                    log.info("Пользователь с email {} успешно добавлен в telegram-чат: chat_id {}", email, chatId);
                    return ResponseEntity.ok("Telegram добавлен к учетной записи");
                })
                .orElseGet(() -> {
                    log.warn("Не удалось добавить Telegram: пользователь с email {} не найден", email);
                    return ResponseEntity.ok("email не найден. Введите корректный email");
                });
    }
}

