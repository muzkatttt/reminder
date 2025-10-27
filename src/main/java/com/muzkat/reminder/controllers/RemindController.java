package com.muzkat.reminder.controllers;

import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.model.User;
import com.muzkat.reminder.service.RemindService;
import com.muzkat.reminder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
 * Использует {@link com.muzkat.reminder.service.RemindService} для выполнения бизнес-логики
 */
@RestController
@RequestMapping("api/remind")
@RequiredArgsConstructor
@Tag(name = "Remind Controller", description = "Обработка запросов на создание, поиск, обновление, удаление напоминаний," +
                                               "сортировка и фильтрация по краткому описанию, дате и времени")
public class RemindController {

    /**
     * Сервис по управлению напоминаниями
     */
    private final RemindService remindService;


    /**
     * Сервис по управлению пользователями
     */
    private final UserService userService;


    /**
     * Получение напоминания по идентификатору
     * @param id идентификатор напоминания
     * @return Optional с DTO напоминания
     */
    @GetMapping("/by-id/{id}")
    @Operation(
            summary = "Поиск по Id напоминания",
            description = "Получение напоминания по Id",
            parameters = {
                    @Parameter(name = "id", description = "Id напоминания")
            }
    )
    public ResponseEntity<RemindDTO> findById(@PathVariable Long id) {
        return remindService.findRemindById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Получение списка напоминаний по описанию
     * @param title краткое описание напоминания
     * @return Optional c DTO напоминания
     */
    @GetMapping("/by-title/{title}")
    @Operation(
            summary = "Поиск по краткому описанию напоминания",
            description = "Получение напоминания по краткому описанию",
            parameters = {
                    @Parameter(name = "title", description = "Краткое описание напоминания")
            }
    )
    public ResponseEntity<RemindDTO> findByTitle(@PathVariable String title) {
        return remindService.findRemindByTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Получение списка напоминаний по описанию
     * @param description описание напоминания
     * @return Optional c DTO напоминаний
     */
    @GetMapping("/by-description/{description}")
    @Operation(
            summary = "Поиск по полному описанию напоминания",
            description = "Получение напоминания по полному описанию",
            parameters = {
                    @Parameter(name = "description", description = "Полное описание напоминания")
            }
    )
    public ResponseEntity<RemindDTO> findByDescription(@PathVariable String description) {
        return remindService.findRemindByDescription(description)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
    @Operation(
            summary = "Создание напоминания",
            description = "Создает новое напоминание для пользователя, прошедшего аутентификацию. В теле запроса " +
                          "принимает объект RemindDTO, содержащий краткое и полное описание, дату и время напоминания",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RemindDTO",
                    required = true,
            content = @Content(schema = @Schema(implementation = RemindDTO.class))),

            responses = {
                    @ApiResponse(responseCode = "201", description = "Напоминание создано"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанными данными не найден"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public ResponseEntity<RemindDTO> createRemind(@Valid @RequestBody RemindDTO remindDTO,
                                                  Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с указанными данными не найден"));

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
    @Operation(
            summary = "Удаление напоминания",
            description = "Удаляет напоминание пользователя, прошедшего аутентификацию",
            parameters = {
                    @Parameter(name = "id", description = "Id напоминания")
            }
    )
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
    @Operation(
            summary = "Обновление напоминания по краткому описанию",
            description = "Обновляет напоминание пользователя, прошедшего аутентификацию. В теле запроса " +
                          "принимает краткое описание напоминания, которое пользователь хочет изменить, " +
                          "и объект RemindDTO, содержащий обновленные краткое и полное описание, дату и время",
            parameters = {
                    @Parameter(name = "title", description = "Краткое описание напоминания")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RemindDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RemindDTO.class)))
    )
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
    @Operation(
            summary = "Обновление напоминания по Id",
            description = "Обновляет напоминание пользователя, прошедшего аутентификацию. В теле запроса " +
                          "принимает Id напоминания, которое пользователь хочет изменить, " +
                          "и объект RemindDTO, содержащий обновленные краткое и полное описание, дату и время",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RemindDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RemindDTO.class)))
    )
    public ResponseEntity<RemindDTO> updateRemindById(@PathVariable Long id, @Valid @RequestBody RemindDTO remindDTO) {
        return remindService.updateRemindById(id, remindDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Получение списка всех напоминаний
     * @return cтатус в случае удачного получения списка напоминаний
     * */
    @GetMapping("/all")
    @Operation(
            summary = "Получение списка всех напоминаний ползователя",
            description = "Получает список всех напоминаний"
    )
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
    @Operation(
            summary = "Фильтрация напоминаний",
            description = "Фильтрует напоминания по краткому описанию (title), дате (date) или времени (time). "
                          + "Все параметры запроса являются необязательными и могут использоваться по отдельности или вместе",
            parameters = {
                    @Parameter(name = "title", description = "Краткое описание напоминания", required = false),
                    @Parameter(name = "date", description = "Дата напоминания в формате YYYY-MM-DD", required = false),
                    @Parameter(name = "time", description = "Время напоминания в формате HH:mm:ss", required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список отфильтрованных напоминаний"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
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
    @Operation(
            summary = "Сортировка напоминаний",
            description = "Сортирует список напоминаний по по краткому описанию (title), дате (date) или времени (time). "
                          + "Все параметры запроса являются необязательными",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список отсортированных напоминаний"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public ResponseEntity<List<RemindDTO>> getSortedReminds(@RequestParam String sortBy) {
        return ResponseEntity.ok(remindService.getSortedReminds(sortBy));
    }
}
