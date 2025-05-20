package com.muzkat.reminder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Представляет учетные данные пользователя, включая связанного пользователя и его зашифрованный пароль
 */
@Entity
@Data
@Table(name = "user_credentials")
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {

    /**
     * Поле уникальный идентификатор учетных данных пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * Поле связанный пользователь
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    /**
     * Поле зашифрованный пароль пользователя
     */
    @Column(name = "password", nullable = false)
    private String password;
}
