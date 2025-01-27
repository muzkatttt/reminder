package com.muzkat.reminder.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

/**
 * Класс пользователя с полями <b>id</b>, <b>name</b>, <b>userEmail</b>
 * @author ekaterinarodionova
 */

@Entity
@Data
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
     * Конструктор - создание нового объекта
     * @param name имя пользователя
     * @param email электронный адрес пользователя
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
