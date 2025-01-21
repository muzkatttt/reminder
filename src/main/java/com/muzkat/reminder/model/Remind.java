package com.muzkat.reminder.model;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class Remind {
    private UUID id; // уникальный идентификатор
    private String title; // краткое описание
    private String description; // полное описание
    private LocalDateTime remind; // дата и время напоминания


    public Remind() {
        super();
    }

    public Remind(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        remind = LocalDateTime.now().plusHours(12);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getRemind() {
        return remind;
    }

    public void setRemind(LocalDateTime remind) {
        this.remind = remind;
    }


    /**
     * Метод проверки валидности при создании напоминания
     * @return true объект создан без пустых полей
     * false не валидный объект
     */
    public boolean doCheckCreateReminder(){
        return (title != null && !title.isEmpty())
                && (description != null && !description.isEmpty());
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Reminder [UUID=").append(id)
                .append(", title=").append(title)
                .append(", description=").append(description)
                .append(", dateRemind=").append(remind);
        return stringBuilder.toString();
    }
}
