package com.muzkat.reminder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO-класс для передачи данных о напоминаниях с разделением даты и времени,
 * а также сортировка и фильтрация.
 * <p>
 *      Класс используется для работы с объектами напоминаний, предоставляя удобную
 *      структуру для передачи данных между слоями приложения.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemindDTO {

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
     * Поле идентификатор пользователя
     */
    private Long userId;

}

