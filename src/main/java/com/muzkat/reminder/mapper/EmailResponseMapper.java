package com.muzkat.reminder.mapper;


import com.muzkat.reminder.dto.EmailResponseDTO;
import com.muzkat.reminder.model.Remind;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct-маппер для преобразования сущности {@link Remind} в DTO {@link EmailResponseDTO}.
 * <p>
 * Выполняет преобразование из {@link Remind}, разбивая поле {@code dateTimeOfRemind}
 * на отдельные поля {@code dateOfRemind} и {@code timeOfRemind}, а также добавляет статус.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface EmailResponseMapper {

    /**
     * Преобразует сущность {@link Remind} в DTO {@link EmailResponseDTO}.
     * Дополнительно разбивает поле dateTimeOfRemind на dateOfRemind и timeOfRemind
     * и задаёт статус отправки письма.
     * @param remind объект сущности для преобразования
     * @param status строка со статусом (например, "Письмо отправлено")
     * @return DTO-объект, соответствующий переданной сущности
     */
    @Mapping(target = "dateOfRemind", expression = "java(remind.getDateTimeOfRemind().toLocalDate())")
    @Mapping(target = "timeOfRemind", expression = "java(remind.getDateTimeOfRemind().toLocalTime())")
    @Mapping(source = "status", target = "status")
    EmailResponseDTO toDto(Remind remind, String status);
}

