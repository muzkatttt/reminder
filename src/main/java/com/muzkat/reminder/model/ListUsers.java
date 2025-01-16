package com.muzkat.reminder.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListUsers {
    private List<User> usersList;

    public ListUsers(List<User> usersList) {
        this.usersList = usersList;
    }
}
