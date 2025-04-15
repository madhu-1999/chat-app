package com.app.chatApp.controller;

import org.springframework.web.bind.annotation.RestController;

import com.app.chatApp.model.UserResponse;
import com.app.chatApp.model.User;
import com.app.chatApp.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerAccount(@RequestBody User user) {
        UserResponse response = null;
        try {
            response = userService.registerAccount(user);
        } catch (Exception e) {
            log.error("Registration unsuccessful!", e);
            return ResponseEntity.internalServerError().build();
        }
        if (!response.isSuccess())
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok().body(response);
    }

    
}
