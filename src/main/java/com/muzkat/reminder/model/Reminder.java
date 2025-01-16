package com.muzkat.reminder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id; // уникальный идентификатор

    private String title; // краткое описание
    private String description; // полное описание

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime remind; // дата и время напоминания
    private int user_id; // идентификатор пользователя
    public Reminder(UUID id, String title, String description, LocalDateTime remind) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.remind = remind;
    }
}
