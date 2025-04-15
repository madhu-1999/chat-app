package com.app.chatApp.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.chatApp.model.UserResponse;
import com.app.chatApp.model.User;
import com.app.chatApp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerAccount(User user) {
        UserResponse response = new UserResponse();
        if (StringUtils.isBlank(user.getUsername()) || userRepository.existsByUsername(user.getUsername())) {
            String msg = String.format("Username %s is either blank or already exists!", user.getUsername());
            log.warn(msg);
            response.setSuccess(false);
            response.setErrMsg("Username is blank or already exists!");
            return response;
        }

        if (StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 8) {
            String msg = String.format("Password is either blank or less than 8 characters!");
            log.warn(msg);
            response.setSuccess(false);
            response.setErrMsg("Password is either blank or less than 8 characters!");
            return response;
        }

        if(StringUtils.isBlank(user.getEmail()) || !isValidEmail(user.getEmail())) {
            String msg = String.format("Email is either blank or invalid!");
            log.warn(msg);
            response.setSuccess(false);
            response.setErrMsg("Email is either blank or invalid!");
            return response;
        }

        com.app.chatApp.entity.User userEntity = new com.app.chatApp.entity.User(user.getUsername(), user.getEmail(),
                passwordEncoder.encode(user.getPassword()));
        com.app.chatApp.entity.User savedUser = userRepository.save(userEntity);
        String msg = String.format("Account registered for username %s email %s", savedUser.getUsername(), savedUser.getEmail());
        log.info(msg);
        response.setSuccess(true);
        response.setUser(User.builder()
            .username(savedUser.getUsername())
            .email(savedUser.getEmail())
            .id(savedUser.getId())
            .build()
        );
        return response;
    }

    public boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

}
