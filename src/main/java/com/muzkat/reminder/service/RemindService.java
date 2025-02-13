package com.muzkat.reminder.service;

import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.repository.RemindRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Сервис для управления напоминаниями.
 * <p>
 *     Класс содержит методы для создания, обновления, удаления,
 * поиска по краткому и полному описанию напоминания,
 * по дате и времени напоминания.
 * </p>
 */
@Service
public class RemindService {

    /**
     * Поле экземпляр {@link RemindRepository}
     */
    private final RemindRepository remindRepository;


    /** Поле сообщение, если по краткому описанию напоминание не найдено */
    private static final String TITLE_NOT_FOUND_MESSAGE = "Не найдено напоминание с title: ";


    /**
     * Поле сообщение, если по полному описанию напоминание не найдено
     */
    private static final String DESCRIPTION_NOT_FOUND_MESSAGE = "Не найдено напоминание по полному описанию: ";


    /**
     * Конструктор - создание нового объекта
     * @param remindRepository Экземпляр класса RemindService
     */
    @Autowired
    public RemindService(RemindRepository remindRepository) {
        this.remindRepository = remindRepository;
    }


    /**
     * Метод для создания напоминания
     * @param remind Напоминание
     * @return Новое напоминание
     */
    public Remind createRemind(Remind remind) {
        return remindRepository.save(remind);
    }


    /**
     * Метод удаления напоминания по id напоминания
     * @param id - идентификатор напоминания
     */
    public void deleteRemind(Long id) {
        if (!remindRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Напоминание с id " + id + " не найдено");
        }
        remindRepository.deleteById(id);
    }


    /**
     * Метод обновления напоминания по краткому описанию
     * @param title Краткое описание напоминания
     * @param remind Напоминание
     * @return Обновленное напоминание
     */
    public Remind updateRemindByTitle(String title, Remind remind) {
        remind.setTitle(title);
        return remindRepository.save(remind);
    }

    /**
     * Метод обновления напоминания по идентификатору
     * @param id Идентификатор напоминания
     * @param remind Напоминание
     * @return Найденное напоминание
     */
    public Remind updateRemindById(Long id, Remind remind) {
        remind.setRemindId(id);
        return remindRepository.save(remind);
    }

    /**
     * Метод поиска напоминания по краткому описанию
     * @param title Краткое описание напоминания
     * @return Найденное напоминание
     */
    public List<Remind> findRemindByTitle(String title) {
        List<Remind> remind = remindRepository.findByTitle(title);
        if (remind == null) {
            throwNotFoundExceptionByTitle(title);
        }
        return remind;
    }


    /**
     * Метод поиска напоминания по полному описанию
     * @param description Полное описание напоминания
     * @return Напоминание
     */
    public List<Remind> findRemindByDescription(String description) {
        List<Remind> remind = remindRepository.findByDescription(description);
        if (remind == null) {
            throwNotFoundExceptionByDescription(description);
        }
        return remind;
    }


    /**
     * Метод поиска напоминания по дате.
     * Находит все записи в диапазоне от 00:00:00 до 23:59:59 указанной даты
     * @param dateTimeOfRemind Дата и время напоминания
     * @return Найденное напоминание
     */
    public List<Remind> findRemindByDate(LocalDate dateTimeOfRemind){
        return remindRepository.findByDateTimeOfRemindBetween(dateTimeOfRemind.atStartOfDay(),
                dateTimeOfRemind.atTime(LocalTime.of(23,59,59)));
    }


    /**
     * Метод поиска напоминания по времени.
     * @param dateTimeOfRemind Дата и время напоминиания
     * @return Найденное напоминание
     */
    public List<Remind> findRemindByTime(LocalTime dateTimeOfRemind){
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

}