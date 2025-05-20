package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.model.User;
import com.muzkat.reminder.service.RemindService;
import com.muzkat.reminder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


/** Контроллер для управления напоминаниями.
 * Обрабатывает запросы на создание, поиск, обновление и удаление напоминаний.
 * Также добавлены методы для получения списка напоминаний, отфильтрованных
 * и отсортированных по краткому описанию, дате и времени.
 * Использует {@link com.muzkat.reminder.service.RemindService} для выполнения бизнес-логики.
 */
@RestController
@RequestMapping("api/remind")
@RequiredArgsConstructor
public class RemindController {

    /**
     * Поле экземпляр RemindService
     */
    private final RemindService remindService;


    /**
     * Поле экземпляр UserService
     */
    private final UserService userService;


    /**
     * Получение напоминания по идентификатору
     * @param id идентификатор напоминания
     * @return Optional с DTO напоминания
     */
    @GetMapping("/by-id/{id}")
    public ResponseEntity<Optional<RemindDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(remindService.findRemindById(id));
    }


    /**
     * Получение списка напоминаний по описанию
     * @param title краткое описание напоминания
     * @return Optional c DTO напоминания
     */
    @GetMapping("/by-title/{title}")
    public ResponseEntity<Optional<RemindDTO>> findByTitle(@PathVariable String title) {
        return ResponseEntity.ok(remindService.findRemindByTitle(title));
    }


    /**
     * Получение списка напоминаний по описанию
     * @param description описание напоминания
     * @return Optional c DTO напоминаний
     */
    @GetMapping("/by-description/{description}")
    public ResponseEntity<Optional<RemindDTO>> findByDescription(@PathVariable String description) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(remindService.findRemindByDescription(description));
    }


    /**
     * Создание нового напоминания для авторизованного пользователя
     * <p>
     *     Извлекает адрес электронной почты пользователя из объекта {@code Authentication},
     *     находит соответствующего пользователя в базе данных и создаёт новое напоминание,
     *     связанное с этим пользователем. Возвращает ответ с кодом 201 (Created) и телом
     *     созданного напоминания
     * </p>
     * @param remindDTO объект {@link RemindDTO}, содержащий данные напоминания
     * @param authentication объект {@link Authentication}, содержащий информацию об авторизованном пользователе
     * @return cозданное напоминание с URI
     * @throws UsernameNotFoundException если пользователь с указанным адресом электронной почты не найден
     */
    @PostMapping("/create")
    public ResponseEntity<RemindDTO> createRemind(@Valid @RequestBody RemindDTO remindDTO,
                                                  Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользоватль с указанными данными не найден"));

        RemindDTO createdRemind = remindService.createRemind(remindDTO, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRemind.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdRemind);
    }


    /**
     * Удаление напоминания по идентификатору
     * @param id идентификатор напоминания
     * @return cтатус, если напоминание успешно удалено
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRemind(@PathVariable Long id) {
        remindService.deleteRemind(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Обновление напоминания по краткому описанию
     * @param title краткое описание напоминания
     * @param remind обновлённый объект напоминания
     * @return Optional с обновлённым DTO, если обновление завершено успешно
     */
    @PutMapping("/by-title/{title}")
    public ResponseEntity<Optional<RemindDTO>> updateRemindByTitle(@PathVariable String title, @Valid @RequestBody RemindDTO remind) {
        return ResponseEntity.ok(remindService.updateRemindByTitle(title, remind));
    }


    /**
     * Обновление напоминания по идентификатору
     * @param id идентификатор напоминания
     * @return обновлённое напоминание в виде DTO, если обновление завершено успешно,
     * либо null-значение, если напоминание не найдено
     */
    @PutMapping("/by-id/{id}")
    public ResponseEntity<Optional<RemindDTO>> updateRemindById(@PathVariable Long id, @Valid @RequestBody RemindDTO remindDTO) {
        return ResponseEntity.ok(remindService.updateRemindById(id, remindDTO));
    }


    /**
     * Получение списка со всеми напоминаниями
     * @return cтатус в случае удачного получения списка напоминаний
     * */
    @GetMapping("/all")
    public ResponseEntity<List<RemindDTO>> getAllReminds() {
        return ResponseEntity.ok(remindService.getAllReminds());
    }


    /**
     * Фильтрация напоминаний по краткому описанию, дате или времени
     * @param title заголовок (необязательный)
     * @param date дата (необязательная)
     * @param time время (необязательное)
     * @return список отфильтрованных напоминаний
     */
    @GetMapping("/filter")
    public ResponseEntity<List<RemindDTO>> filterReminds(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        return ResponseEntity.ok(remindService.filterReminds(title, date, time));
    }


    /**
     * Сортировка напоминаний по краткому описанию, дате или времени
     * @param sortBy критерий сортировки ("title", "date" или "time")
     * @return отсортированный список напоминаний
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<RemindDTO>> getSortedReminds(@RequestParam String sortBy) {
        return ResponseEntity.ok(remindService.getSortedReminds(sortBy));
    }
}
