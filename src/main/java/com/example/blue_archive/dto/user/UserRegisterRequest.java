package com.example.blue_archive.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {
    private String userId;
    private String password;
    private String nickname;
    private String email;    // 선택
    private String phone;    // 선택
    private String gender;   // 선택
}
