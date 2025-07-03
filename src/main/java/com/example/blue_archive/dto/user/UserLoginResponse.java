package com.example.blue_archive.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String message;
    private String userId;
    private String token;
}
