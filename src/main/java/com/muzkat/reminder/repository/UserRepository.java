package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностями типа User.
 * Данное расширение {@link UserRepository} позволяет выполнять
 * стандартные CRUD-операции, содержит дополнительные методы
 * по поиску пользователя по имени и по адресу электронной почты
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Поиск пользователя по его имени
     * @param name
     * @return user
     */
    User findByName(String name);

    /**
     * Поиск пользователя по email-адресу
     * @param email
     * @return user
     */
    User findByEmail(String email);

    /**
     * Метод проверяет, существует ли пользователь с таким же email перед созданием нового пользователя
     * @param email адрес электронной почты
     * @return  true существует
     *          false не существует
     */
    boolean existsByEmail(String email);
}
