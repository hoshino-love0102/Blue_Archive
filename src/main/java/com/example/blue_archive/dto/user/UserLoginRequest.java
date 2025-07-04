package com.example.blue_archive.dto.user;

import lombok.Getter;
import lombok.Setter;

// 사용자 로그인 요청 DTO
@Getter
@Setter
public class UserLoginRequest {

    // 사용자 ID
    private String userId;

    // 비밀번호
    private String password;
}
