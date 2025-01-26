package com.muzkat.reminder.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import java.time.LocalDateTime;


/**
 * Класс напоминания со свойствами <b>id</b>, <b>title</b>, <b>description</b>, <b>dateTimeOfRemind</b>.
 *  @author ekaterinarodionova
 */

@Entity
@Data
@Table(name= "reminds")
public class Remind {

    /**
     * Конструктор - создание нового объекта
     * @param title краткое описание
     * @param description полное описание
     * @param dateTimeOfRemind дата и время отправки напоминания пользователю
     * @param userId идентификатор пользователя
     */
    public Remind(String title, String description, LocalDateTime dateTimeOfRemind, Long userId) {
        this.title = title;
        this.description = description;
        this.dateTimeOfRemind = dateTimeOfRemind;
        this.userId = userId;
    }

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
    @Column(name="title")
    private String title;


    /**
     * Поле полное описание напоминания
     */
    @Column(name="description")
    private String description;


    /**
     * Поле дата и время напоминания
     */
    @Column(name="remind")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimeOfRemind;


    /**
     * Поле идентификатор пользователя
     */
    @Column(name="user_id")
    private Long userId;

    public Long getRemindId() {
        return remindId;
    }

    public void setRemindId(Long remindId) {
        this.remindId = remindId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
