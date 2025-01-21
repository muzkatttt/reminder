package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.Remind;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReminderRepository {
    List<Remind> list = new ArrayList<>();

    public ReminderRepository(List<Remind> list) {
        this.list = list;
    }

    /**
     * Метод создания напоминания
     * @return Remind
     */
    public Remind createRemind(){
        return null;
    }

    /**
     * Метод редактирования напоминания
     */
    public Remind correctRemind(Remind remind){
        // TO DO добавить тело метода
        return remind;
    }


    /**
     * Поиск напоминания по названию, описанию, дате, времени)
     * Сортировка (по названию, дате, времени)
     * Фильтр (по дате, времени)
     * Список должен выводиться с пагинацией
     * При наступлении даты и времени напоминаний, приложение должно посылать уведомления
     * с Заголовком сущности и описанием,  на email и telegram
     * (Настройки адреса пользователя и его контакта в telegram должны задаваться в профиле пользователя).
     */
}
