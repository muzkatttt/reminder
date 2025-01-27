package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.Remind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


/**
 * Репозиторий для работы с сущностями типа {@Link Remind}.
 * Данное расширение {@link JpaRepository} позволяет выполнять
 * стандартные CRUD-операции. Добавлены дополнительные методы по поиску
 * по краткому и полному описанию напоминания, по дате и по времени
 */
@Repository
public interface RemindRepository extends JpaRepository<Remind, Long> {

    /**
     * Поиск напоминания по краткому описанию
     * @param title
     * @return remind
     */
    Remind findByTitle(String title);


    /**
     * Поиск напоминания по полному описанию
     * @param description
     * @return remind
     */
    Remind findByDescription(String description);


    /**
     * Поиск напоминания по дате и времени
     * @param dateTimeOfRemind
     * @return remind
     */
    Remind findByDateTimeOfRemind(LocalDateTime dateTimeOfRemind);

}
