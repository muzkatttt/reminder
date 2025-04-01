package com.muzkat.reminder.controllers;

import com.muzkat.reminder.model.User;
import com.muzkat.reminder.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/** Контроллер для управления пользователями.
 * Обрабатывает запросы на создание, поиск, обновление и удаление пользователей.
 * Использует {@link com.muzkat.reminder.service.UserService} для выполнения бизнес-логики.
 */
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@AllArgsConstructor
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
    public ResponseEntity<Optional<User>> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userservice.findById(id));
    }


    /**
     * Создание нового пользователя
     * @param user объект пользователя
     * @return Optional с созданным пользователем или пустой, если пользователь не создан
     */
    @PostMapping("/create")
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
    public Optional<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userservice.updateUser(id, user);
    }


    /**
     * Удаление пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return true, если пользователь успешно удалён, иначе false
     */
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userservice.deleteById(id);
    }


    /**
     * Проверка существования пользователя по адресу электронной почты
     * @param email адрес электронной почты
     * @return true, если пользователь существует
     */
    @GetMapping("/exists")
    public boolean existsByEmail(@RequestParam String email) {
        return userservice.existsByEmail(email);
    }

}
