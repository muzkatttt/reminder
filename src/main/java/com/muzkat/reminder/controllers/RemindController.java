package com.muzkat.reminder.controllers;

import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.service.RemindService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/** Контроллер для управления напоминаниями.
 * Обрабатывает запросы на создание, поиск, обновление и удаление напоминаний.
 * Использует {@link com.muzkat.reminder.service.RemindService} для выполнения бизнес-логики.
 * Обрабатывает запросы на создание, поиск, обновление и удаление напоминаний.
 */
@RestController
@RequestMapping("api/remind")
public class RemindController {

    /**
     * Поле экземпляр RemindService
     */
    private final RemindService remindService;

    /**
     * Конструктор для внедрения зависимости RemindService
     * @param remindService Cервис управления напоминаниями
     */
    @Autowired
    public RemindController(RemindService remindService) {
        this.remindService = remindService;
    }


    /**
     * <p>
     *     Получение списка напоминаний по заголовку
     * </p>
     * Проверить метод: GET <a href="http://localhost:8080/api/remind/by-title/first">...</a>
     * @param title заголовок напоминания
     * @return Список найденных напоминаний
     */
    @GetMapping("/by-title/{title}")
    public ResponseEntity<List<Remind>> findByTitle(@PathVariable String title) {
        return ResponseEntity.status(HttpStatus.OK).body(remindService.findRemindByTitle(title));
    }


    /**
     * <p>
     *     Получение списка напоминаний по описанию
     * </p>
     * Проверить метод: GET <a href="http://localhost:8080/api/remind/by-description/five description">...</a>
     * @param description описание напоминания
     * @return Список найденных напоминаний
     */

    @GetMapping("/by-description/{description}")
    public ResponseEntity<List<Remind>> findByDescription(@PathVariable String description) {
        return ResponseEntity.status(HttpStatus.OK).body(remindService.findRemindByDescription(description));
    }


    /**
     * <p>
     *     Получение списка напоминаний по дате
     * </p>
     * Проверить метод в Postman: GET <a href="http://localhost:8080/api/remind/by-date?dateTimeOfRemind=2025-02-02">...</a>
     * @param dateTimeOfRemind дата напоминания в формате ISO
     * @return Cписок найденных напоминаний
     */
    @GetMapping("/by-date")
    public ResponseEntity<List<Remind>> findByDate(@RequestParam(name = "dateTimeOfRemind") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTimeOfRemind) {
        return ResponseEntity.ok(remindService.findRemindByDate(dateTimeOfRemind));
    }

    /**<p>
     *     Получение списка напоминаний по времени
     * </p>
     * Проверить метод в Postman: GET <a href="http://localhost:8080/api/remind/by-time?dateTimeOfRemind=00:00:00">...</a>
     * @param dateTimeOfRemind время напоминания в формате ISO
     * @return Cписок найденных напоминаний
     */
    @GetMapping("/by-time")
    public ResponseEntity<List<Remind>> findByTime(@RequestParam(name = "dateTimeOfRemind") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime dateTimeOfRemind) {
        return ResponseEntity.ok(remindService.findRemindByTime(dateTimeOfRemind));
    }


    /**
     * <p>
     *     Создание нового напоминания
     * </p>
     * Проверить метод в Postman: POST http://localhost:8080/api/remind
     * @param remind объект напоминания
     * @return Cозданное напоминание с URI
     */
    @PostMapping
    public ResponseEntity<Remind> createRemind(@RequestBody Remind remind) {
        Remind createRemind = remindService.createRemind(remind);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createRemind.getRemindId())
                .toUri();
        return ResponseEntity.created(location).body(createRemind);
    }


    /**
     * <p>
     *     Удаление напоминания по идентификатору
     * </p>
     * @param id идентификатор напоминания
     * @return Cтатус, если напоминание успешно удалено
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRemind(@PathVariable Long id) {
        remindService.deleteRemind(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * <p>
     *     Обновление напоминания по краткому описанию
     * </p>
     * Проверить метод в Postman: PUT <a href="http://localhost:8080/api/remind/by-title/five">...</a>
     * @param title заголовок напоминания
     * @param remind обновлённый объект напоминания
     * @return Cтатус, если обновление завершено успешно
     */
    @PutMapping("/by-title/{title}")
    public ResponseEntity<Remind> updateRemindByTitle(@PathVariable String title, @Valid @RequestBody Remind remind) {
        return ResponseEntity.ok(remindService.updateRemindByTitle(title, remind));
    }


    /**
     * <p>
     *     Обновление напоминания по идентификатору
     * </p>
     * Проверить метод в Postman: PUT <a href="http://localhost:8080/api/remind/by-id/31">...</a>
     * @param id идентификатор напоминания
     * @return Cтатус, если обновление завершено успешно
     */
    @PutMapping("/by-id/{id}")
    public ResponseEntity<Remind> updateRemindById(@PathVariable Long id, @Valid @RequestBody Remind remind) {
        return ResponseEntity.ok().body(remindService.updateRemindById(id, remind));
    }
}
