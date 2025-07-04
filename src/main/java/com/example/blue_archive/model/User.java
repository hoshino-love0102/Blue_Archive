package com.example.blue_archive.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 사용자 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users") // 테이블명을 users로 지정
public class User {

    // 사용자 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인 ID (유니크, 필수)
    @Column(unique = true, nullable = false)
    private String userId;

    // 비밀번호 (필수)
    @Column(nullable = false)
    private String password;

    // 닉네임 (필수)
    @Column(nullable = false)
    private String nickname;

    // 이메일 (유니크, 선택)
    @Column(unique = true)
    private String email;

    // 핸드폰 번호 (유니크, 선택)
    @Column(unique = true)
    private String phone;

    // 성별 (선택)
    private String gender;

    // 사용자 권한 (기본값 USER)
    private String role = "USER";

    // 가입 일시 (생성 시 자동 입력)
    private LocalDateTime createdAt = LocalDateTime.now();
}
