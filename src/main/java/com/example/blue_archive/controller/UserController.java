package com.example.blue_archive.controller;

import com.example.blue_archive.dto.user.UserLoginRequest;
import com.example.blue_archive.dto.user.UserLoginResponse;
import com.example.blue_archive.dto.user.UserRegisterRequest;
import com.example.blue_archive.jwt.JwtTokenProvider;
import com.example.blue_archive.model.User;
import com.example.blue_archive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// 사용자 인증 관련 API 컨트롤러
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    // 사용자 레포지토리
    private final UserRepository userRepository;

    // 비밀번호 암호화 빈
    private final PasswordEncoder passwordEncoder;

    // JWT 토큰 발급 클래스
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        // 이미 존재하는 아이디인지 체크
        if (userRepository.existsByUserId(request.getUserId())) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }

        // User 엔티티 생성 후 정보 저장
        User user = new User();
        user.setUserId(request.getUserId());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setRole("ROLE_USER"); // 기본 권한 설정

        // DB 저장
        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        // 사용자 조회
        User user = userRepository.findByUserId(request.getUserId());
        // 사용자가 없거나 비밀번호가 틀린 경우
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(user.getUserId(), user.getRole());

        // 응답 DTO 생성
        UserLoginResponse response = new UserLoginResponse(
                "로그인 성공",
                user.getUserId(),
                token
        );

        return ResponseEntity.ok(response);
    }
}
