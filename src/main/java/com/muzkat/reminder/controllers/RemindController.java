package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.RemindDTO;
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
 * Также добавлены методы для получения списка напоминаний, отфильтрованных
 * и отсортированных по краткому описанию, дате и времени.
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
     *     Создание нового напоминания
     * </p>
     * Проверить метод в Postman: POST http://localhost:8080/api/remind
     * @param remind объект напоминания
     * @return Cозданное напоминание с URI
     */
    @PostMapping()
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


    /** <p>
     *      Получение списка со всеми напоминаниями
     * </p>
     * Проверить метод в Postman: GET <a href="http://localhost:8080/api/remind/all">...</a>
     * @return Статус в случае удачного получения списка напоминаний
     * */
    @GetMapping("/all")
    public ResponseEntity<List<RemindDTO>> getAllReminds() {
        return ResponseEntity.ok(remindService.getAllReminds());
    }


    /** <p>
     *      Фильтрация напоминаний
     * </p>
     * Проверить метод в Postman по краткому описанию:
     * GET <a href="http://localhost:8080/api/remind/filter?title=second">...</a>
     * по дате: GET <a href="http://localhost:8080/api/remind/filter?date=2025-02-02">...</a>
     * по времени: GET <a href="http://localhost:8080/api/remind/filter?time=20:00:00">...</a>
     */
    @GetMapping("/filter")
    public ResponseEntity<List<RemindDTO>> filterReminds(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        return ResponseEntity.ok(remindService.filterReminds(title, date, time));
    }


    /** Сортировка напоминаний
     * Проверить метод в Postman по краткому описанию: <a href="http://localhost:8080/api/remind/sorted?sortBy=title">...</a>
     * по дате: <a href="http://localhost:8080/api/remind/sorted?sortBy=date">...</a>
     * по времени: <a href="http://localhost:8080/api/remind/sorted?sortBy=time">...</a>
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<RemindDTO>> getSortedReminds(@RequestParam String sortBy) {
        return ResponseEntity.ok(remindService.getSortedReminds(sortBy));
    }
}
