package com.app.chatApp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
    private boolean success;
    private String errMsg;
    private User user;
}
