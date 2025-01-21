package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.Remind;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReminderBuilder implements ReminderBuilderInterface{

    private UUID id; // уникальный идентификатор
    private String title; // краткое описание
    private String description; // полное описание
    private LocalDateTime remind; // дата и время напоминания

    public ReminderBuilder() {
        super();
    }

    @Override
    public ReminderBuilderInterface fixUUID() {
        System.out.println("Для проверки работы приложения");
        this.id = UUID.randomUUID();
        return this;
    }

    @Override
    public ReminderBuilderInterface fixTitle() {
        System.out.println("Здесь добавляю заголовок напоминания");
        this.title = "Reminder title";
        return this;
    }

    @Override
    public ReminderBuilderInterface fixDescription() {
        System.out.println("А здесь и само напоминание :)");
        this.description = "Description body";
        return this;
    }

    @Override
    public ReminderBuilderInterface fixRemind() {
        System.out.println("Добавили дату напоминания");
        this.remind = LocalDateTime.now().plusHours(12); // на время добавлю такое время, потом исправлю
        return null;
    }

    @Override
    public Remind build() {
        Remind remind = new Remind(id, title, description);
        if(remind.doCheckCreateReminder()){
            return remind;
        } else {
            System.out.println("Не удалось создать заметку!");
        }
        return null;
    }
}
