package com.muzkat.reminder.controllers;

import com.muzkat.reminder.model.User;
import com.muzkat.reminder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {


    /**
     * Поле экземпляр UserService
     */
    private UserService userservice;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable long userId){
        return ResponseEntity.ok().body(userservice.findById(userId));
    }

}
