package com.muzkat.reminder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.muzkat.reminder.model.Remind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO-класс для представления результата отправки напоминания {@link Remind}
 * по электронной почте с разделением даты и времени
 * <p>
 *      Класс используется в качестве ответа контроллера после успешной отправки письма.
 *      Предоставляет структуру для передачи данных между слоями приложения.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponseDTO {

    /**
     * Поле уникальный идентификатор напоминания
     */
    private Long id;


    /**
     * Поле краткое описание напоминания
     */
    private String title;


    /**
     * Поле полное описание напоминания
     */
    private String description;


    /**
     * Поле дата напоминания
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfRemind;


    /**
     * Поле время напоминания
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime timeOfRemind;


    /**
     * Поле статус отправки напоминания
     */
    private String status;
}
