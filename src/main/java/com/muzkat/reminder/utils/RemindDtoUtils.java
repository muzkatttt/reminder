package com.muzkat.reminder.utils;

import com.muzkat.reminder.dto.RemindDTO;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;


/**
 * Утилитарный класс для выполнения для фильтрации и сортировки напоминаний.
 * Предоставляет статические методы для:
 * <ul>
 *     <li>Сортировки списка {@link RemindDTO} по заголовку, дате или времени</li>
 *     <li>Фильтрации напоминаний по заголовку, дате и/или времени</li>
 * </ul>
 * Класс не предназначен для создания экземпляров.
 */
@UtilityClass
public class RemindDtoUtils {

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
            default -> throw new IllegalArgumentException("Некорректный параметр сортировки: " + sortBy);
        };
    }


    /**
     * Метод проверяет, соответствует ли объект заданным критериям фильтрации.
     * @param dto DTO для проверки
     * @param titleFilter фильтр по заголовку (может быть null)
     * @param dateFilter фильтр по дате (может быть null)
     * @param timeFilter фильтр по времени (может быть null)
     * @return true, если напоминание соответствует всем указанным фильтрам
     */
    public static boolean matchesFilters(RemindDTO dto, String titleFilter, LocalDate dateFilter, LocalTime timeFilter) {
        if (titleFilter != null && !dto.getTitle().toLowerCase().contains(titleFilter.toLowerCase())) return false;
        if (dateFilter != null && !dto.getDateOfRemind().equals(dateFilter)) return false;
        if (timeFilter != null && !dto.getTimeOfRemind().equals(timeFilter)) return false;
        return true;
    }
}
