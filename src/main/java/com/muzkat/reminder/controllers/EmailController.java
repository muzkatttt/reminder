package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.EmailResponseDTO;
import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.service.RemindService;
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
public class EmailController {

    /**
     * Поле экземпляр RemindService
     */
    private final RemindService remindService;


    /**
     * Отправка напоминания по электронной почте
     * @param remindId id напоминания
     * @return статус, если напоминание успешно отправлено
     */
    @PostMapping("/send/{remindId}")
    public ResponseEntity<EmailResponseDTO> sendRemindToEmail(@PathVariable Long remindId) {
        EmailResponseDTO response = remindService.sendRemindById(remindId);
        return ResponseEntity.ok(response);
    }
}
