package com.muzkat.reminder.service;

import com.muzkat.reminder.dto.EmailResponseDTO;
import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.mapper.EmailResponseMapper;
import com.muzkat.reminder.mapper.RemindMapper;
import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.model.User;
import com.muzkat.reminder.repository.RemindRepository;
import com.muzkat.reminder.repository.UserRepository;
import com.muzkat.reminder.service.notification.EmailSendService;
import com.muzkat.reminder.utils.RemindDtoUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


import static com.muzkat.reminder.utils.RemindDtoUtils.getComparator;
import static com.muzkat.reminder.utils.RemindDtoUtils.matchesFilters;

/**
 * Сервис для управления напоминаниями.
 * <p>
 *     Содержит бизнес-логику создания, обновления, удаления и поиска напоминаний
 * по краткому и полному описанию, дате и времени.
 * Обрабатывает отправку напоминаний по электронной почте
 * </p>
 * Исключения обрабатываются на уровне контроллеров
 * через {@code Optional} и глобальные перехватчики
 */
@Service
@AllArgsConstructor
public class RemindService {

    /**
     * Поле экземпляр {@link RemindRepository}
     */
    private final RemindRepository remindRepository;

    /**
     * Поле экземпляр {@link RemindMapper}
     */
    private final RemindMapper remindMapper;

    /**
     * Поле экземпляр {@link UserRepository}
     */
    private final UserRepository userRepository;

    /**
     * Поле экземпляр {@link EmailSendService}
     */
    private final EmailSendService emailSendService;

    /**
     * Поле экземпляр {@link TelegramService}
     */
    private final TelegramService telegramService;

    /**
     * Поле экземпляр {@link EmailResponseMapper}
     */
    private final EmailResponseMapper emailResponseMapper;

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
        Remind remind = remindMapper.toEntity(remindDTO);
        Remind savedRemind = remindRepository.save(remind);
        return remindMapper.toDto(savedRemind);
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
        return remindRepository.findById(id).map(remindMapper::toDto);
    }


    /**
     * Метод поиска напоминания по заголовку.
     * @param title заголовок напоминания
     * @return {@link Optional} с {@link RemindDTO}, если найдено
     */
    public Optional<RemindDTO> findRemindByTitle(String title) {
        return remindRepository.findByTitle(title)
                .stream()
                .findFirst()
                .map(remindMapper::toDto);
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
            existRemind.setDateTimeOfRemind(remindDTO.getDateOfRemind().atTime(remindDTO.getTimeOfRemind()));
        }

        Remind savedRemind = remindRepository.save(existRemind);

        return Optional.of(remindMapper.toDto(savedRemind));
    }


    /**
     * Метод обновления напоминания по идентификатору
     * <p>
     *     Метод ищет в базе данных напоминание по идентификатору,
     *     проверяет, не являются ли поля {@link Remind} null: краткое описание,
     *     полное описание, дату и время напоминания. И если проверка пройдена, то обновляет напоминание
     * </p>
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
            LocalDate date = remindDTO.getDateOfRemind();
            LocalTime time = remindDTO.getTimeOfRemind();
            existRemind.setDateTimeOfRemind(date.atTime(time));
        }

        Remind updateRemind = remindRepository.save(existRemind);
        RemindDTO resultDto = remindMapper.toDto(updateRemind);
        return Optional.of(resultDto);
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
                .map(remindMapper::toDto);
    }


    /**
     * Метод для получения списка всех напоминаний в виде списка {@link RemindDTO}
     * @return cписок всех напоминаний в форме DTO
     */
    public List<RemindDTO> getAllReminds() {
        return remindRepository.findAll().stream()
                .map(remindMapper::toDto)
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
                .map(remindMapper::toDto)
                .filter(dto -> matchesFilters(dto, titleFilter, dateFilter, timeFilter))
                .collect(Collectors.toList());
    }


    /**
     * Метод получает все напоминания, сортирует их с помощью компаратора
     * из {@link RemindDtoUtils#getComparator(String)},
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
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }


    /**
     * Метод отправляет напоминание по электронной почте по заданному идентификатору {@link Remind}.
     * <p>
     * Выполняет следующие действия:
     * <ul>
     *     <li>Извлекает напоминание из базы данных по идентификатору</li>
     *     <li>Находит пользователя, связанного с напоминанием</li>
     *     <li>Отправляет письмо с темой и содержимым напоминания</li>
     *     <li>Устанавливает флаг {@code notified = true} и сохраняет напоминание</li>
     *     <li>Формирует и возвращает DTO-ответ для клиента</li>
     * </ul>
     * </p>
     * @param remindId идентификатор напоминания
     * @return DTO с отправленным напоминанием и статусом доставки
     * @throws NoSuchElementException если напоминание или пользователь не найдены
     */
    public EmailResponseDTO sendRemindById(Long remindId) {
        Remind remind = remindRepository.findById(remindId).orElseThrow();
        User user = userRepository.findById(remind.getUserId()).orElseThrow();

        emailSendService.sendEmail(
                user.getEmail(),
                "Напоминание: " + remind.getTitle(),
                remind.getDescription()
        );

        remind.setNotified(true);
        remindRepository.save(remind);
        return emailResponseMapper.toDto(remind, "Письмо отправлено");
    }
}
