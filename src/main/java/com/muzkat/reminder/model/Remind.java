package com.muzkat.reminder.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;


/**
 * Класс напоминания со свойствами <b>id</b>, <b>title</b>, <b>description</b>, <b>dateTimeOfRemind</b>.
 *  @author ekaterinarodionova
 */

@Entity
@Data
@Table(name= "remind")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Remind {

    /**
     * Поле уникальный идентификатор напоминания
     */
    @Id
    @Column(name = "remind_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long remindId;


    /**
     * Поле краткое описание напоминания
     */
    @Column(name="title", nullable = false)
    private String title;


    /**
     * Поле полное описание напоминания
     */
    @Column(name="description", nullable = false)
    private String description;


    /**
     * Поле дата и время напоминания
     */
    @Column(name="remind", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimeOfRemind;


    /**
     * Поле идентификатор пользователя
     */
    @Column(name="user_id")
    private Long userId;


    /**
     * Конструктор - создание нового объекта
     * @param title краткое описание
     * @param description полное описание
     * @param dateTimeOfRemind дата и время отправки напоминания пользователю
     * @param userId идентификатор пользователя
     */
    @Autowired
    public Remind(String title, String description, LocalDateTime dateTimeOfRemind, Long userId) {
        this.title = title;
        this.description = description;
        this.dateTimeOfRemind = dateTimeOfRemind;
        this.userId = userId;
    }


    /**
     * Конструктор - создание нового объекта
     * @param title Краткое описание
     * @param description Полное описание
     * @param dateTimeOfRemind Дата и время отправки напоминания пользователю
     */
    public Remind(String title, String description, LocalDateTime dateTimeOfRemind) {
        this.title = title;
        this.description = description;
        this.dateTimeOfRemind = dateTimeOfRemind;
    }
}
