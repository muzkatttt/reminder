package com.muzkat.reminder.service;

import com.muzkat.reminder.model.User;
import com.muzkat.reminder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Сервис для управления пользователями.
 * <p>
 *     Класс содержит методы для создания,
 * обновления, удаления и поиска пользователя.</p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * Метод создания нового пользователя
     * @param user объект пользователя для сохранения в базе данных
     * @return сохраненный объект пользователя
     */
    public User createUser(User user){
        user.setId(user.getId());
        return userRepository.save(user);
    }

    /**
     * Метод для обновления пользователя в базе данных
     * @param id идентификатор пользователя, которого нужно обновить
     * @param user объект пользователя с обновленными данными
     * @return обновленный объект пользователя
     */
    public User updateUser(long id, User user){
        checkExistsById(id);
        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * Метод для удаления пользователя из базы данных по его идентификатору.
     * @param id идентификатор пользователя, которого нужно удалить
     */
    public void deleteById(long id){
        checkExistsById(id);
        userRepository.deleteById(id);
    }


    /**
     * Метод поиска пользователя по его идентификатору
     * @param id идентификатор пользователя
     * @return объект пользователя (если он найден)
     */
    public User findById(long id){
        User user = userRepository.findById(id).orElse(null);
        if (user==null){
            throwNotFoundExceptionById(id);
        }
        return user;
    }

    /**
     * Метод проверки существования пользователя с указанным идентификатором.
     * @param id идентификатор пользователя для проверки
     */
    private void checkExistsById(long id){
        if(!userRepository.existsById(id)){
            throwNotFoundExceptionById(id);
        }
    }

    /**
     * Исключение, если пользователь с идентификатором не найден.
     * @param id идентификатор пользователя, по которому найдено совпадение
     * @throws ResponseStatusException всегда
     */
    private void throwNotFoundExceptionById(long id){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE + id);
    }

    /** Поле экземпляр UserRepository */
    private final UserRepository userRepository;


    /** Статическое поле - сообщение, если пользователь не найден */
    public static final String NOT_FOUND_MESSAGE = "Не удалось найти пользователя c id=";

}
