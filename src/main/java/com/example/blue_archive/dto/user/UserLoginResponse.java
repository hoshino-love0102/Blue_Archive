package com.example.blue_archive.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 사용자 로그인 응답 DTO
@Getter
@AllArgsConstructor
public class UserLoginResponse {

    // 로그인 결과 메시지
    private String message;

    // 사용자 ID
    private String userId;

    // 인증 토큰
    private String token;
}
