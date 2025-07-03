package com.example.blue_archive.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true)
    private String email;    // 이메일 (선택)

    @Column(unique = true)
    private String phone;    // 핸드폰 번호 (선택)

    private String gender;   // 성별 (선택)

    private String role = "USER";

    private LocalDateTime createdAt = LocalDateTime.now();
}
