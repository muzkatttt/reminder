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
     * Получение списка напоминаний по описанию
     * @param description описание напоминания
     * @return Список найденных напоминаний
     */
    @GetMapping("/by-description/{description}")
    public ResponseEntity<RemindDTO> findByDescription(@PathVariable String description) {
        return ResponseEntity.status(HttpStatus.OK).body(remindService.findRemindByDescription(description));
    }


    /**
     * Создание нового напоминания
     * @param remindDTO объект напоминания
     * @return Cозданное напоминание с URI
     */
    @PostMapping("/create")
    public ResponseEntity<RemindDTO> createRemind(@RequestBody RemindDTO remindDTO) {
        Remind createdRemind = remindService.createRemind(RemindDTO.fromEntity(remindDTO.toEntity())).toEntity();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRemind.getRemindId())
                .toUri();
        return ResponseEntity.created(location).body(RemindDTO.fromEntity(createdRemind));
    }


    /**
     * Удаление напоминания по идентификатору
     * @param id идентификатор напоминания
     * @return Cтатус, если напоминание успешно удалено
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRemind(@PathVariable Long id) {
        remindService.deleteRemind(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Обновление напоминания по краткому описанию
     * @param title заголовок напоминания
     * @param remind обновлённый объект напоминания
     * @return Cтатус, если обновление завершено успешно
     */
    @PutMapping("/by-title/{title}")
    public ResponseEntity<RemindDTO> updateRemindByTitle(@PathVariable String title, @Valid @RequestBody RemindDTO remind) {
        return ResponseEntity.ok(remindService.updateRemindByTitle(title, remind));
    }


    /**
     * Обновление напоминания по идентификатору
     * @param id идентификатор напоминания
     * @return Cтатус, если обновление завершено успешно
     */
    @PutMapping("/by-id/{id}")
    public ResponseEntity<RemindDTO> updateRemindById(@PathVariable Long id, @Valid @RequestBody RemindDTO remindDTO) {
        Remind updatedRemind = remindService.updateRemindById(id, remindDTO.toEntity());
        return ResponseEntity.ok(RemindDTO.fromEntity(updatedRemind));
    }


    /**
     * Получение списка со всеми напоминаниями
     * @return Статус в случае удачного получения списка напоминаний
     * */
    @GetMapping("/all")
    public ResponseEntity<List<RemindDTO>> getAllReminds() {
        return ResponseEntity.ok(remindService.getAllReminds());
    }


    /**
     * Фильтрация напоминаний по краткому описанию, дате или времени
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
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<RemindDTO>> getSortedReminds(@RequestParam String sortBy) {
        return ResponseEntity.ok(remindService.getSortedReminds(sortBy));
    }
}
