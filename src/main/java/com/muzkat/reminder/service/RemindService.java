package com.muzkat.reminder.service;

import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.repository.RemindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/**
 * Сервис для управления напоминаниями.
 * <p>
 *     Класс содержит методы для создания, обновления, удаления,
 * поиска по краткому и полному описанию напоминания,
 * по дате и времени напоминания.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RemindService {

    /**
     * Метод для создания напоминания
     * @param remind
     * @return remind
     */
    public Remind createRemind(Remind remind) {
        remind.setRemindId(remind.getRemindId());
        return remindRepository.save(remind);
    }


    /**
     * Метод удаления напоминания по id напоминания
     * @param id
     */
    public void deleteRemind(Long id) {
        remindRepository.deleteById(id);
    }


    /**
     * Метод обновления напоминания по краткому описанию
     * @param title краткое описание напоминания
     * @param remind напоминание
     * @return remind
     */
    public Remind updateRemindByTitle(String title, Remind remind) {
        remind.setTitle(title);
        return remindRepository.save(remind);
    }

    /**
     * Метод обновления напоминания по идентификатору
     * @param id идентификатор напоминания
     * @param remind напоминание
     * @return remind
     */
    public Remind updateRemindById(Long id, Remind remind) {
        remind.setRemindId(id);
        return remindRepository.save(remind);
    }

    /**
     * Метод поиска напоминания по краткому описанию
     * @param title краткое описание напоминания
     * @return remind
     */
    public Remind findRemindByTitle(String title) {
        Remind remind = remindRepository.findByTitle(title);
        if (remind == null) {
            throwNotFoundExceptionByTitle(title);
        }
        return remind;
    }


    /**
     * Метод поиска напоминания по полному описанию
     * @param description
     * @return remind
     */
    public Remind findRemindByDescription(String description) {
        Remind remind = remindRepository.findByDescription(description);
        if (remind == null) {
            throwNotFoundExceptionByDescription(description);
        }
        return remind;
    }


    /**
     * Метод поиска напоминания по дате и времени
     * @param dateTimeOfRemind
     * @return remind
     */
    public Remind findRemindByDateTime(LocalDateTime dateTimeOfRemind) {
        return remindRepository.findByDateTimeOfRemind(dateTimeOfRemind);
    }


    /**
     * Исключение, если по краткому описанию напоминание не найдено
     * @param title
     */
    private void throwNotFoundExceptionByTitle(String title) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, TITLE_NOT_FOUND_MESSAGE + title);
    }


    /**
     * Исключение, если по полному описанию напоминание не найдено
     * @param description
     */
    private void throwNotFoundExceptionByDescription(String description) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, DESCRIPTION_NOT_FOUND_MESSAGE + description);
    }


    /**
     * Поле экземпляр {@link RemindRepository}
     */
    private RemindRepository remindRepository;


    /** Поле сообщение, если по краткому описанию напоминание не найдено */
    private static final String TITLE_NOT_FOUND_MESSAGE = "Не найдено напоминание с title: ";


    /**
     * Поле сообщение, если по полному описанию напоминание не найдено
     */
    private static final String DESCRIPTION_NOT_FOUND_MESSAGE = "Не найдено напоминание по полному описанию: ";

}