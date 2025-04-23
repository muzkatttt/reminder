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

}
