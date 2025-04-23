package com.muzkat.reminder.mapper;

import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.model.Remind;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct-маппер для преобразования между сущностью {@link Remind} и DTO {@link RemindDTO}.
 * Выполняет преобразование:
 * <li>из {@link Remind} в {@link RemindDTO} — разбивает LocalDateTime на LocalDate и LocalTime</li>
 * <li>из {@link RemindDTO} в {@link Remind} — объединяет LocalDate и LocalTime в LocalDateTime</li>
 */
@Mapper(componentModel = "spring")
public interface RemindMapper {

    /**
     * Преобразует сущность {@link Remind} в DTO {@link RemindDTO}.
     * Дополнительно разбивает поле dateTimeOfRemind на dateOfRemind и timeOfRemind
     * @param remind объект сущности для преобразования
     * @return DTO-объект, соответствующий переданной сущности
     */
    @Mapping(target = "dateOfRemind", expression = "java(remind.getDateTimeOfRemind().toLocalDate())")
    @Mapping(target = "timeOfRemind", expression = "java(remind.getDateTimeOfRemind().toLocalTime())")
    RemindDTO toDto(Remind remind);


    /**
     * Преобразует DTO {@link RemindDTO} в сущность {@link Remind}.
     * Объединяет поля dateOfRemind и timeOfRemind в одно поле dateTimeOfRemind
     * @param dto DTO-объект для преобразования
     * @return cущность, соответствующая переданному DTO
     */
    @Mapping(target = "dateTimeOfRemind", expression = "java(dto.getDateOfRemind().atTime(dto.getTimeOfRemind()))")
    Remind toEntity(RemindDTO dto);
}
