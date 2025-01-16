package com.muzkat.reminder.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID user_id;
    private String name;
    private String user_email;

    public User(UUID user_id, String name) {
        this.user_id = user_id;
        this.name = name;
    }

    public User(UUID user_id, String name, String user_email) {
        this.user_id = user_id;
        this.name = name;
        this.user_email = user_email;
    }
}
