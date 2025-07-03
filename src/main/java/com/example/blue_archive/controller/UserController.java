package com.example.blue_archive.controller;

import com.example.blue_archive.dto.user.UserLoginRequest;
import com.example.blue_archive.dto.user.UserLoginResponse;
import com.example.blue_archive.jwt.JwtTokenProvider;
import com.example.blue_archive.model.User;
import com.example.blue_archive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByUserId(user.getUserId())) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        User user = userRepository.findByUserId(request.getUserId());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getUserId());

        UserLoginResponse response = new UserLoginResponse(
                user.getUserId(),
                token,
                "로그인 성공"
        );

        return ResponseEntity.ok(response);
    }
}
