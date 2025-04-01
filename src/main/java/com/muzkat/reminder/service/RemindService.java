package com.muzkat.reminder.service;

import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.repository.RemindRepository;
import com.muzkat.reminder.utils.RemindDtoUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для управления напоминаниями.
 * <p>
 *     Класс содержит методы для создания, обновления, удаления,
 * поиска по краткому и полному описанию напоминания,
 * по дате и времени напоминания.
 * Исключения не выбрасываются напрямую — используется Optional и boolean,
 * обработка ошибок происходит на уровне контроллера.
 * </p>
 */
@Service
@AllArgsConstructor
public class RemindService {

    /**
     * Поле экземпляр {@link RemindRepository}
     */
    private final RemindRepository remindRepository;


    /**
     * Метод для создания напоминания
     * <p>
     *     Метод преобразует DTO в сущность {@link Remind},
     *     сохраняет его в базе данных и возвращает новое напоминание
     * </p>
     * @param remindDTO напоминание
     * @return новое напоминание в виде {@link RemindDTO}
     */
    public RemindDTO createRemind(RemindDTO remindDTO) {
        Remind remind = RemindDtoUtils.toEntity(remindDTO);
        Remind savedRemind = remindRepository.save(remind);
        return RemindDtoUtils.fromEntity(savedRemind);
    }


    /**
     * Метод удаления напоминания по id напоминания
     * @param id - идентификатор напоминания
     * @return true, если удаление прошло успешно; false, если напоминание не найдено
     */
    public boolean deleteRemind(Long id) {
        if (!remindRepository.existsById(id)) {
            return false;
        }
        remindRepository.deleteById(id);
        return true;
    }


    /**
     * Метод поиска напоминания по идентификатору
     * @param id идентификатор напоминания
     * @return {@link Optional} с {@link RemindDTO}, если найдено
     */
    public Optional<RemindDTO> findRemindById(Long id) {
        return remindRepository.findById(id).map(RemindDtoUtils::fromEntity);
    }


    /**
     * Метод поиска напоминания по заголовку.
     * @param title заголовок напоминания
     * @return {@link Optional} с {@link RemindDTO}, если найдено
     */
    public Optional<RemindDTO> findRemindByTitle(String title) {
        Optional<Remind> remind = remindRepository.findByTitle(title).stream().findFirst();
        return RemindDtoUtils.fromOptionalEntity(remind);
    }


    /**
     * Метод обновления напоминания по краткому описанию
     * <p>
     *     Метод ищет в базе данных напоминание по краткому описанию,
     *     проверяет, не являются ли поля {@link Remind} null: краткое описание,
     *     полное описание, дату и время напоминания. И если проверка пройдена, то обновляет напоминание
     * </p>
     * @param title Краткое описание напоминания
     * @param remindDTO DTO с новыми данными
     * @return {@link Optional} с обновлённым {@link RemindDTO}, если найдено
     */
    public Optional<RemindDTO> updateRemindByTitle(String title, RemindDTO remindDTO) {
        Optional<Remind> optionalRemind = remindRepository.findByTitle(title)
                .stream()
                .findFirst();
        if (optionalRemind.isEmpty()) {
            return Optional.empty();
        }

        Remind existRemind = optionalRemind.get();

        if (remindDTO.getTitle() != null) {
            existRemind.setTitle(remindDTO.getTitle());
        }
        if (remindDTO.getDescription() != null) {
            existRemind.setDescription(remindDTO.getDescription());
        }
        if (remindDTO.getDateOfRemind() != null && remindDTO.getTimeOfRemind() != null) {
            existRemind.setDateTimeOfRemind(RemindDtoUtils.toLocalDateTime(remindDTO));
        }

        Remind savedRemind = remindRepository.save(existRemind);

        return Optional.of(RemindDtoUtils.fromEntity(savedRemind));
    }


    /**
     * Метод обновления напоминания по идентификатору
     * <p>
     *     Метод ищет в базе данных напоминание по идентификатору,
     *     проверяет, не являются ли поля {@link Remind} null: краткое описание,
     *     полное описание, дату и время напоминания. И если проверка пройдена, то обновляет напоминание
     *      * </p>
     * @param id Идентификатор существующего напоминания
     * @param remindDTO DTO с новыми данными
     * @return {@link Optional} с обновлённым {@link RemindDTO}, если найдено
     */
    public Optional<RemindDTO> updateRemindById(Long id, RemindDTO remindDTO) {
        Optional<Remind> optionalRemind = remindRepository.findById(id);

        if (optionalRemind.isEmpty()) {
            return Optional.empty();
        }
        Remind existRemind = optionalRemind.get();
        if (remindDTO.getTitle() != null) {
            existRemind.setTitle(remindDTO.getTitle());
        }
        if (remindDTO.getDescription() != null) {
            existRemind.setDescription(remindDTO.getDescription());
        }
        if (remindDTO.getDateOfRemind() != null && remindDTO.getTimeOfRemind() != null) {
            existRemind.setDateTimeOfRemind(RemindDtoUtils.toLocalDateTime(remindDTO));
        }
        Remind updateRemind = remindRepository.save(existRemind);
        return Optional.of(RemindDtoUtils.fromEntity(updateRemind));
    }


    /**
     * Метод поиска напоминания по полному описанию
     * @param description полное описание напоминания
     * @return {@link Optional} с {@link RemindDTO}, если найдено
     */
    public Optional<RemindDTO> findRemindByDescription(String description) {
        return remindRepository.findByDescription(description)
                .stream()
                .findFirst()
                .map(RemindDtoUtils::fromEntity);
    }


    /**
     * Метод преобразования класса {@link Remind} в {@link RemindDTO}
     * @param remind напоминание
     * @return напоминание, преобразованное в {@link RemindDTO}
     */
    private RemindDTO convertToDTO(Remind remind) {
        return RemindDtoUtils.fromEntity(remind);
    }


    /**
     * Метод для получения списка всех напоминаний в виде списка {@link RemindDTO}
     * @return cписок всех напоминаний в форме DTO
     */
    public List<RemindDTO> getAllReminds() {
        return remindRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    /**
     * Метод извлекает все напоминания из репозитория , преобразует в DTO
     * и применяет фильтр к списку напоминаний по краткому описанию, дате и времени напоминания.
     * @param titleFilter фильтр по краткому описанию напоминания (может быть null)
     * @param dateFilter фильтр по дате напоминания (может быть null)
     * @param timeFilter фильтр по времени напоминания (может быть null)
     * @return список напоминаний, которые удовлетворяют критериям фильтра
     */
    public List<RemindDTO> filterReminds(String titleFilter, LocalDate dateFilter, LocalTime timeFilter) {
        return remindRepository.findAll().stream()
                .map(RemindDtoUtils::fromEntity)
                .filter(dto -> RemindDtoUtils.matchesFilters(dto, titleFilter, dateFilter, timeFilter))
                .collect(Collectors.toList());
    }


    /**
     * Метод получает все напоминания, сортирует их с помощью компаратора из {@link RemindDtoUtils#getComparator(String)},
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
                .sorted(RemindDtoUtils.getComparator(sortBy))
                .collect(Collectors.toList());
    }
}

