package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.Remind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * Репозиторий для работы с сущностями типа {@link Remind}.
 * Данное расширение {@link JpaRepository} позволяет выполнять
 * стандартные CRUD-операции. Добавлены дополнительные методы по поиску
 * по краткому и полному описанию напоминания, по дате и по времени
 */
@Repository
public interface RemindRepository extends JpaRepository<Remind, Long> {

    /**
     * Метод поиска напоминания по краткому описанию
     * @param title краткое описание напоминания
     * @return список напоминаний
     */
    List<Remind> findByTitle(String title);


    /**
     * Метод поиска напоминания по полному описанию
     * @param description полное описание напоминания
     * @return список напоминаний
     */
    List<Remind> findByDescription(String description);

}
