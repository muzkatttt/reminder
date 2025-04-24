package com.muzkat.reminder.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс пользователя с полями <b>id</b>, <b>name</b>, <b>email</b>, <b>telegramChatId</b>
 * @author ekaterinarodionova
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="app_user")
public class User {

    /**
     * Поле уникальный идентификатор пользователя
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * Поле имя пользователя
     */
    @Column(name = "name")
    private String name;


    /**
     * Поле адрес электронной почты пользователя
     */
    @Column(name="user_email")
    private String email;

    /**
     * Поле id чата для отправки уведомлений пользователю
     */
    @Column(name = "telegram_chat_id")
    private String telegramChatId;
}
