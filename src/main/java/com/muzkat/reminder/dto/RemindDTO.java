package com.muzkat.reminder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

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
public class RemindDTO {

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
     * Конструктор - создание нового объекта {@link RemindDTO}
     * @param title Краткое описание
     * @param description Полное описание
     * @param dateOfRemind Дата отправки напоминания пользователю
     * @param timeOfRemind Время отправки напоминания пользователю
     */
    public RemindDTO(String title, String description, LocalDate dateOfRemind, LocalTime timeOfRemind) {
        this.title = title;
        this.description = description;
        this.dateOfRemind = dateOfRemind;
        this.timeOfRemind = timeOfRemind;
    }


    /**
     * Метод возвращает компаратор для сортировки напоминаний по заданному критерию.
     * @param sortBy Критерий сортировки. Возможные значения:
     *                <ul>
     *                 <li>"title" – сортировка по заголовку</li>
     *                   <li>"date" – сортировка по дате</li>
     *                <li>"time" – сортировка по времени</li>
     *            </ul>
     * @return Компаратор для сортировки
     */
    public static Comparator<RemindDTO> getComparator(String sortBy) {
        return switch (sortBy) {
            case "title" -> Comparator.comparing(RemindDTO::getTitle);
            case "date" -> Comparator.comparing(RemindDTO::getDateOfRemind);
            case "time" -> Comparator.comparing(RemindDTO::getTimeOfRemind);
            default -> throw new IllegalArgumentException(String.format("Некорректный параметр сортировки %s", sortBy));
        };
    }


    /**
     * Метод проверяет, соответствует ли объект заданным критериям фильтрации.
     * @param titleFilter фильтр по заголовку (может быть null)
     * @param dateFilter фильтр по дате (может быть null)
     * @param timeFilter фильтр по времени (может быть null)
     * @return true, если напоминание соответствует всем указанным фильтрам
     */
    public boolean matchesFilters(String titleFilter, LocalDate dateFilter, LocalTime timeFilter) {
        if (titleFilter != null && !title.toLowerCase().contains(titleFilter.toLowerCase())) {
            return false;
        }
        if (dateFilter != null && !dateOfRemind.equals(dateFilter)) {
            return false;
        }
        if (timeFilter != null && !timeOfRemind.equals(timeFilter)) {
            return false;
        }
        return true;
    }
}