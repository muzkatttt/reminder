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
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE + user.getEmail());
        }
        user.setId(null);
        validateUser(user);
        return userRepository.save(user);
    }


    /**
     * Метод для обновления пользователя в базе данных
     * @param id идентификатор пользователя, которого нужно обновить
     * @param user объект пользователя с обновленными данными
     * @return обновленный объект пользователя
     */
    public User updateUser(Long id, User user){
        checkExistsById(id);
        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * Метод для удаления пользователя из базы данных по его идентификатору.
     * @param id идентификатор пользователя, которого нужно удалить
     */
    public void deleteById(Long id){
        checkExistsById(id);
        userRepository.deleteById(id);
    }

    /**
     * Метод поиска пользователя по его идентификатору
     * @param id идентификатор пользователя
     * @return объект пользователя (если он найден)
     */
    public User findById(Long id){
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
    private void checkExistsById(Long id){
        if(!userRepository.existsById(id)){
            throwNotFoundExceptionById(id);
        }
    }

    /**
     * Метод проверки существования пользователя с указанным адресом электронной почты.
     * @param email адрес электронной почты пользователя для проверки
     */
    private void checkExistsByEmail(String email){
        if(!userRepository.existsByEmail(email)){
            throwNotFoundExceptionByEmail(email);
        }
    }

    /**
     * Метод для валидации пользователя по адресу электронной почты и по имени
     * @param user поьзователь, для которого осуществляется проверка
     */
    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email должен быть указан!");
        }
        if(user.getName() == null || user.getName().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя пользователя должно быть указано!");
        }
    }

    /**
     * Исключение, если пользователь с идентификатором не найден.
     * @param id идентификатор пользователя, по которому найдено совпадение
     * @throws ResponseStatusException всегда
     */
    private void throwNotFoundExceptionById(Long id){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE + id);
    }

    /**
     * Исключение, если пользователь с указанным адресом электронной почты не найден.
     * @param email адрес электронной почты пользователя, по которому найдено совпадение
     * @throws ResponseStatusException всегда
     */
    private void throwNotFoundExceptionByEmail(String email){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE + email);
    }

    /** Поле экземпляр UserRepository */
    private UserRepository userRepository;


    /** Статическое поле - сообщение, если пользователь не найден */
    public static final String NOT_FOUND_MESSAGE = "Не удалось найти пользователя c: ";

}


