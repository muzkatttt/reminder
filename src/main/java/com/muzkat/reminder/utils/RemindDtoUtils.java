package com.muzkat.reminder.utils;

import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.model.Remind;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Optional;


/**
 * Утилитарный класс для выполнения преобразований между сущностями {@link Remind} и DTO {@link RemindDTO},
 *  содержит статические методы, вспомогательные функции для фильтрации и сортировки.
 */
public class RemindDtoUtils {

    /**
     * Метод преобразует {@link RemindDTO} в {@link Remind}
     * <p>
     *      Метод используется для преобразования DTO в основную сущность перед сохранением в базу данных
     * </p>
     * @return Напоминание {@link Remind} с установленными значениями
     */
    public static Remind toEntity(RemindDTO dto) {
        Remind remind = new Remind();
        remind.setTitle(dto.getTitle());
        remind.setDescription(dto.getDescription());
        remind.setDateTimeOfRemind(toLocalDateTime(dto));
        remind.setUserId(dto.getUserId());
        return remind;
    }


    /**
     * Метод преобразует сущность {@link Remind} в {@link RemindDTO}.
     * @param remind напоминаниt
     * @return {@link RemindDTO} с раздельными полями даты и времени
     * @throws NullPointerException если remind равен null
     */
    public static RemindDTO fromEntity(Remind remind) {
        return new RemindDTO(
                remind.getRemindId(),
                remind.getTitle(),
                remind.getDescription(),
                remind.getDateTimeOfRemind().toLocalDate(),
                remind.getDateTimeOfRemind().toLocalTime(),
                remind.getUserId()
        );
    }


    /**
     * Метод преобразует Optional-сущности в Optional-DTO, проверяет наличие значения до преобразования
     * @param optionalRemind Optional с Remind
     * @return Optional с RemindDTO или Optional.empty()
     */
    public static Optional<RemindDTO> fromOptionalEntity(Optional<Remind> optionalRemind) {
        if (optionalRemind.isEmpty()) {
            return Optional.empty();
        }
        return optionalRemind.map(RemindDtoUtils::fromEntity);
    }


    /**
     * Метод преобразует dateOfRemind и timeOfRemind в {@link LocalDateTime}
     * @param dto DTO с полями даты и времени
     * @return дата и время в формате {@link LocalDateTime}
     * @throws IllegalStateException если дата или время равны null
     */

    public static LocalDateTime toLocalDateTime(RemindDTO dto) {
        return dto.getDateOfRemind().atTime(dto.getTimeOfRemind());
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
        if (titleFilter != null && !dto.getTitle().toLowerCase().contains(titleFilter.toLowerCase())) {
            return false;
        }
        if (dateFilter != null && !dto.getDateOfRemind().equals(dateFilter)) {
            return false;
        }
        if (timeFilter != null && !dto.getTimeOfRemind().equals(timeFilter)) {
            return false;
        }
        return true;
    }
}
