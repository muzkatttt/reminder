package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Репозиторий для работы с сущностями типа User.
 * Данное расширение {@link UserRepository} позволяет выполнять
 * стандартные CRUD-операции, содержит дополнительные методы
 * по поиску пользователя по адресу электронной почты
 * и по проверке существования пользователя при создании новой учетной записи в БД
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Метод проверяет, существует ли пользователь с таким же email перед созданием нового пользователя
     * @param email адрес электронной почты
     * @return  true существует
     *          false не существует
     */
    boolean existsByEmail(String email);


    /**
     * Метод ищет пользователя по адресу электронной почты
     * @param email адрес электронной почты пользователя
     * @return {@link Optional} с найденным {@link User}, если пользователь существует,
     *      иначе возвращает пустой {@link Optional}
     */
    Optional<User> findByEmail(String email);

}
