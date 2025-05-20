package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Репозиторий для работы с сущностями типа {@link UserCredentials}.
 * Данное расширение {@link JpaRepository} позволяет выполнять
 * стандартные CRUD-операции
 */
@Repository
public interface UserCredentialsRepository  extends JpaRepository <UserCredentials, Long>{

    /**
     * Метод поиска учетных данных пользователя по его идентификатору
     * @param userId идентификатор пользователя
     * @return {@link Optional}, содержащий {@link UserCredentials}, если пользователь найден,
     * иначе возвращает пустой {@link Optional}
     */
    Optional<UserCredentials> findByUserId(Long userId);
}