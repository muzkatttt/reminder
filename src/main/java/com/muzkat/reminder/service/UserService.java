package com.muzkat.reminder.service;

import com.muzkat.reminder.model.User;
import com.muzkat.reminder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Сервис для управления пользователями.
 * <p>
 *     Класс содержит методы для создания, обновления, удаления пользователя.
 * Исключения не выбрасываются напрямую — используется Optional и boolean,
 * обработка ошибок происходит на уровне контроллера.</p>
 * </p>
 */
@Service
@AllArgsConstructor
public class UserService {

    /** Поле экземпляр {@link UserRepository} */
    private UserRepository userRepository;


    /**
     * Метод создания нового пользователя
     * @param user объект пользователя для сохранения в базе данных
     * @return сохраненный объект пользователя или Optional.empty(),
     * если email уже существует или данные некорректны
     */
    public Optional<User> createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return Optional.empty();
        }
        if (!isUserValid(user)) {
            return Optional.empty();
        }
        user.setId(null);
        return Optional.of(userRepository.save(user));
    }

    /**
     * Метод для обновления пользователя в базе данных
     * @param id идентификатор пользователя, которого нужно обновить
     * @param user объект пользователя с обновленными данными
     * @return обновленный объект пользователя или Optional.empty(), если пользователь не найден
     */
    public Optional<User> updateUser(Long id, User user) {
        if (!userRepository.existsById(id)) {
            return Optional.empty();
        }
        user.setId(id);
        return Optional.of(userRepository.save(user));
    }


    /**
     * Метод для удаления пользователя из базы данных по его идентификатору.
     * @param id идентификатор пользователя, которого нужно удалить
     * @return true, если пользователь был удалён, иначе false
     */
    public boolean deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }


    /**
     * Метод поиска пользователя по его идентификатору
     * @param id идентификатор пользователя
     * @return Optional с объектом пользователя, если найден
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    /**
     * Метод проверки существования пользователя с указанным адресом электронной почты.
     * @param email адрес электронной почты пользователя для проверки
     * @return true, если пользователь найден
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    /**
     * Метод для валидации пользователя по адресу электронной почты и по имени:
     * имя и email не должны быть пустыми
     * @param user пользователь, для которого осуществляется проверка
     */
    private boolean isUserValid(User user) {
        return user.getEmail() != null && !user.getEmail().isEmpty()
                && user.getName() != null && !user.getName().isEmpty();
    }
}
