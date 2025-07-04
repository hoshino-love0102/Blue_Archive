package com.example.blue_archive.dto.user;

import lombok.Getter;
import lombok.Setter;

// 사용자 회원가입 요청 DTO
@Getter
@Setter
public class UserRegisterRequest {

    // 사용자 ID
    private String userId;

    // 비밀번호
    private String password;

    // 닉네임
    private String nickname;

    // 이메일 (선택)
    private String email;

    // 전화번호 (선택)
    private String phone;

    // 성별 (선택)
    private String gender;
}
