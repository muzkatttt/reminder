package com.muzkat.reminder.controllers;

import com.muzkat.reminder.model.User;
import com.muzkat.reminder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/** Контроллер для управления пользователями.
 * Обрабатывает запросы на создание, поиск, обновление и удаление пользователей.
 * Использует {@link com.muzkat.reminder.service.UserService} для выполнения бизнес-логики
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@AllArgsConstructor
@Tag(name = "User Controller", description = "Обработка запросов на создание, поиск, обновление и удаление пользователя")
public class UserController {


    /**
     * Поле экземпляр {@link UserService}
     */
    private UserService userservice;


    /**
     * Получение пользователя по идентификатору.
     * @param id идентификатор пользователя
     * @return Optional с пользователем или пустой, если не найден
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Поиск пользователя по Id",
            description = "Получение пользователя по Id"
    )
    public ResponseEntity<Optional<User>> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userservice.findById(id));
    }


    /**
     * Создание нового пользователя
     * @param user объект пользователя
     * @return Optional с созданным пользователем или пустой, если пользователь не создан
     */
    @PostMapping("/create")
    @Operation(
            summary = "Создание учетной записи пользователя",
            description = "Создает нового пользователя в системе. В теле запроса принимает объект User, содержащий имя," +
                          " email пользователя, Id чата для отправки уведомлений пользователю",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User",
                    required = true),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Учетная запись создана"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public Optional<User> createUser(@Valid @RequestBody User user) {
        return userservice.createUser(user);
    }


    /**
     * Обновление пользователя по идентификатору
     * @param id идентификатор пользователя
     * @param user объект пользователя с новыми данными
     * @return Optional с обновлённым пользователем или пустой, если не найден
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление данных пользователя по Id",
            description = "Обновляет учетные данные пользователя. В теле запроса принимает Id пользователя, " +
                          "учетные данные которого необходимо обновить, и объект User, содержащий имя, " +
                          "email пользователя, Id чата для отправки уведомлений пользователю",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User",
                    required = true
            )
    )
    public Optional<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userservice.updateUser(id, user);
    }


    /**
     * Удаление пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return true, если пользователь успешно удалён, иначе false
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление учетной записи пользователя из приложения",
            description = "Удаляет учетную запись пользователя, прошедшего аутентификацию"
    )
    public boolean deleteUser(@PathVariable Long id) {
        return userservice.deleteById(id);
    }


    /**
     * Проверка существования пользователя по адресу электронной почты
     * @param email адрес электронной почты
     * @return true, если пользователь существует
     */
    @GetMapping("/exists")
    @Operation(
            summary = "Проверка существования пользователя по email",
            description = "Проверяет, существует ли пользователь с указанным email в приложении"
    )
    public boolean existsByEmail(@RequestParam String email) {
        return userservice.existsByEmail(email);
    }

}
