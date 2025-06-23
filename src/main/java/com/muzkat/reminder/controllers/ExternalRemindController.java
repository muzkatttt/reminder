package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.service.ExternalRemindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для получения напоминаний из внешнего сервиса.
 * Делает GET-запрос к внешнему API через {@link ExternalRemindService}
 * и возвращает массив напоминаний
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/external")
@Tag(name = "External Remind", description = "Обращение через внешний API")
public class ExternalRemindController {

    /**
     * Сервис для обращения к внешнему API
     */
    private final ExternalRemindService externalRemindService;

    /**
     * Метод возвращает список напоминаний, полученных из внешнего API
     * @return массив объектов {@link RemindDTO}
     */
    @GetMapping("/reminds")
    @Operation(
            summary = "Получение списка напоминаний",
            description = "Получает массив напоминаний"
    )
    public RemindDTO[] getExternalReminds(){
        return externalRemindService.fetchAllReminds();
    }
}
