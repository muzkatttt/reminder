package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.Remind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


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
     * @return Список напоминаний
     */
    List<Remind> findByTitle(String title);


    /**
     * Поиск напоминания по полному описанию
     * @param description
     * @return Список напоминаний
     */
    List<Remind> findByDescription(String description);


    /**
     * Поиск напоминания по дате.
     * @param startDateOfRemind Начало временного диапазона (начало дня, 00:00).
     * @param endDateOfRemind Конец временного диапазона (конец дня, 23:59:59).
     * @return Список напоминаний, запланированных в течение указанного дня.
     */
    List<Remind> findByDateTimeOfRemindBetween(
            @Param("startDateOfRemind") LocalDateTime startDateOfRemind,
            @Param("endDateOfDay") LocalDateTime endDateOfRemind);


    /**
     * Поиск напоминания по времени.
     * @param dateTimeOfRemind
     * @return Список найденных напоминаний.
     */
    List<Remind> findByDateTimeOfRemind(@Param("time")LocalTime dateTimeOfRemind);

}
