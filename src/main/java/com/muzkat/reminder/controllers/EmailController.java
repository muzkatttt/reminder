package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.EmailResponseDTO;
import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.service.RemindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Контроллер для отправки напоминаний по электронной почте
 *  <p>
 *     Обрабатывает HTTP-запросы на отправку письма, связанного с напоминанием {@link Remind}.
 *     Делегирует выполнение бизнес-логики в {@link com.muzkat.reminder.service.RemindService}
 *  </p>
 */
@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor
@Tag(name = "Email Sender", description = "Отправка уведомлений пользователю по электронной почте")
public class EmailController {

    /**
     * Сервис для управления отправкой напоминаний на email пользователя
     */
    private final RemindService remindService;


    /**
     * Отправка напоминания по электронной почте
     * @param remindId id напоминания
     * @return статус, если напоминание успешно отправлено
     */
    @PostMapping("/send/{remindId}")
    @Operation(
            summary = "Отправка напоминания по email",
            description = "Отправляет напоминание по электронной почте пользователя",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "remindId",
                            description = "Id напоминания для отправки",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Письмо успешно отправлено"),
                    @ApiResponse(responseCode = "400", description = "Некорректный email или напоминание уже было отправлено"),
                    @ApiResponse(responseCode = "404", description = "Напоминание или пользователь не найдены"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public ResponseEntity<EmailResponseDTO> sendRemindToEmail(@PathVariable Long remindId) {
        EmailResponseDTO response = remindService.sendRemindById(remindId);
        return ResponseEntity.ok(response);
    }
}
