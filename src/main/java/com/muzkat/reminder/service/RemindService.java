package com.muzkat.reminder.service;

import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.repository.RemindRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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


    /**
     * Конструктор - создание нового объекта
     * @param remindRepository экземпляр класса RemindService
     */
    @Autowired
    public RemindService(RemindRepository remindRepository) {
        this.remindRepository = remindRepository;
    }


    /**
     * Метод для создания напоминания
     * <p>
     *     Метод преобразует DTO в сущность {@link Remind},
     *     сохраняет его в базе данных и возвращает новое напоминание
     * </p>
     * @param remindDTO напоминание
     * @return новое напоминание
     */
    public RemindDTO createRemind(RemindDTO remindDTO) {
        Remind remind = remindDTO.toEntity();
        Remind savedRemind = remindRepository.save(remind);
        return RemindDTO.fromEntity(savedRemind);
    }


    /**
     * Метод удаления напоминания по id напоминания
     * @param id - идентификатор напоминания
     */
    public void deleteRemind(Long id) {
        if (!remindRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Напоминание с id %d не найдено!", id));
        }
        remindRepository.deleteById(id);
    }


    /**
     * Метод обновления напоминания по краткому описанию
     * <p>
     *     Метод ищет в базе данных напоминание по краткому описанию,
     *     проверяет, не являются ли поля {@link Remind} null: краткое описание,
     *     полное описание, дату и время напоминания. И если проверка пройдена, то обновляет напоминание
     * </p>
     * @param title Краткое описание напоминания
     * @param remindDTO Напоминание
     * @return Обновленное напоминание
     */
    public RemindDTO updateRemindByTitle(String title, RemindDTO remindDTO) {
        Remind existRemind = remindRepository.findByTitle(title)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Напоминание с заголовком %s не найдено!",title)));

        if (remindDTO.getTitle() != null) {
            existRemind.setTitle(remindDTO.getTitle());
        }
        if (remindDTO.getDescription() != null) {
            existRemind.setDescription(remindDTO.getDescription());
        }
        if (remindDTO.getDateOfRemind() != null && remindDTO.getTimeOfRemind() != null) {
            existRemind.setDateTimeOfRemind(remindDTO.toLocalDateTime());
        }

        Remind savedRemind = remindRepository.save(existRemind);

        return RemindDTO.fromEntity(savedRemind);
    }


    /**
     * Метод обновления напоминания по идентификатору
     * <p>
     *     Метод ищет в базе данных напоминание по идентификатору,
     *     проверяет, не являются ли поля {@link Remind} null: краткое описание,
     *     полное описание, дату и время напоминания. И если проверка пройдена, то обновляет напоминание
     *      * </p>
     * @param id Идентификатор напоминания
     * @param remind Напоминание
     * @return Найденное напоминание
     */
    public Remind updateRemindById(Long id, Remind remind) {
        Remind existRemind = remindRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Напоминание с id %d не найдено!", id)));
        if (remind.getTitle() != null) {
            existRemind.setTitle(remind.getTitle());
        }
        if (remind.getDescription() != null) {
            existRemind.setDescription(remind.getDescription());
        }
        if (remind.getDateTimeOfRemind() != null) {
            existRemind.setDateTimeOfRemind(remind.getDateTimeOfRemind());
        }
        return remindRepository.save(existRemind);
    }


    /**
     * Метод поиска напоминания по полному описанию
     * @param description Полное описание напоминания
     * @return Напоминание
     */
    public RemindDTO findRemindByDescription(String description) {
    Remind remind = remindRepository.findByDescription(description)
            .stream()
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException(
                    String.format("Напоминание с описанием '%s' не найдено!", description)
            ));

    return RemindDTO.fromEntity(remind);
    }


    /**
     * Метод преобразования класса {@link Remind} в {@link RemindDTO}
     * @param remind Напоминание
     * @return Напоминание, преобразованное в {@link RemindDTO}
     */
    private RemindDTO convertToDTO(Remind remind) {
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
     * Метод для получения списка всех напоминаний в виде списка {@link RemindDTO}
     * @return Список всех напоминаний
     */
    public List<RemindDTO> getAllReminds() {
        return remindRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    /**
     * Метод извлекает все напоминания из репозитория , преобразует в DTO
     * и применяет фильтр к списку напоминаний по краткому описанию, дате и времени напоминания.
     * @param titleFilter фильтр по заголовку напоминания (может быть null)
     * @param dateFilter фильтр по дате напоминания (может быть null)
     * @param timeFilter фильтр по времени напоминания (может быть null)
     * @return список напоминаний, которые удовлетворяют критериям фильтра
     */
    public List<RemindDTO> filterReminds(String titleFilter, LocalDate dateFilter, LocalTime timeFilter) {
        return remindRepository.findAll().stream()
                .map(this::convertToDTO)
                .filter(dto -> dto.matchesFilters(titleFilter, dateFilter, timeFilter))
                .collect(Collectors.toList());
    }


    /**
     * Метод получает все напоминания, сортирует их с помощью компаратора из {@link RemindDTO#getComparator(String)},
     * и возвращает отсортированный список по указанному критерию
     * @param sortBy Критерий сортировки. Возможные значения:
     *               <ul>
     *                   <li>"title" – сортировка по заголовку</li>
     *                   <li>"date" – сортировка по дате</li>
     *                   <li>"time" – сортировка по времени</li>
     *               </ul>
     * @return отсортированный список напоминаний в формате {@link RemindDTO}
     * @throws IllegalArgumentException если передан неверный параметр сортировки
     */
    public List<RemindDTO> getSortedReminds(String sortBy) {
        return getAllReminds().stream()
                .sorted(RemindDTO.getComparator(sortBy))
                .collect(Collectors.toList());
    }

}