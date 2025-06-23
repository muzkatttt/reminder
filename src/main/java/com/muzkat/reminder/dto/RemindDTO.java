package com.muzkat.reminder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO-класс для передачи данных о напоминаниях с разделением даты и времени.
 * <p>
 *      Класс используется для работы с объектами напоминаний, предоставляя удобную
 *      структуру для передачи данных между слоями приложения
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO-класс для передачи данных о напоминаниях с разделением даты и времени")
public class RemindDTO {

    /**
     * Поле уникальный идентификатор напоминания
     */
    @Schema(description = "Id напоминания", example = "13")
    private Long id;

    /**
     * Поле краткое описание напоминания
     */
    @Schema(description = "Краткое описание напоминания", example = "Тренировка 06.06")
    private String title;

    /**
     * Поле полное описание напоминания
     */
    @Schema(description = "Полное описание напоминания", example = "Тренировка 06.06, не забудь кроссовки")
    private String description;

    /**
     * Поле дата напоминания
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Дата напоминания в формате yyyy-MM-dd")
    private LocalDate dateOfRemind;

    /**
     * Поле время напоминания
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Schema(description = "Время напоминания в формате HH:mm:ss")
    private LocalTime timeOfRemind;

    /**
     * Поле идентификатор пользователя
     */
    @Schema(description = "Id пользователя")
    private Long userId;
}

