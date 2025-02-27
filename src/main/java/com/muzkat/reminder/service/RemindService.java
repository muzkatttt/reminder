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
     * @param remind напоминание
     * @return новое напоминание
     */
    public Remind createRemind(Remind remind) {
        return remindRepository.save(remind);
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
     * @param title Краткое описание напоминания
     * @param remind Напоминание
     * @return Обновленное напоминание
     */
    public Remind updateRemindByTitle(String title, Remind remind) {
        Remind existRemind = remindRepository.findByTitle(title).getFirst();
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
     * Метод обновления напоминания по идентификатору
     * @param id Идентификатор напоминания
     * @param remind Напоминание
     * @return Найденное напоминание
     */
    public Remind updateRemindById(Long id, Remind remind) {
        Remind existRemind = remindRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Напоминание с id %d не найдено!", id)));
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
    public List<Remind> findRemindByDescription(String description) {
        List<Remind> remind = remindRepository.findByDescription(description);
        if (remind == null) {
            throw new EntityNotFoundException(String.format("Напоминание по краткому описанию %s не найдено!", description));
        }
        return remind;
    }


    /**
     * Метод преобразования класса {@link Remind} в {@link RemindDTO}
     * @param remind Напоминание
     * @return Напоминание, преобразованное в {@link RemindDTO}
     */
    private RemindDTO convertToDTO(Remind remind) {
        return new RemindDTO(
                remind.getTitle(),
                remind.getDescription(),
                remind.getDateTimeOfRemind().toLocalDate(),
                remind.getDateTimeOfRemind().toLocalTime()
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