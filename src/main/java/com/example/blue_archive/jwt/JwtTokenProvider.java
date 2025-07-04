package com.example.blue_archive.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// JWT 관련 기능을 제공하는 클래스
@Component
public class JwtTokenProvider {

    // 비밀 키 (토큰 서명에 사용)
    private final Key secretKey;

    // 토큰 유효 시간 (1시간)
    private final long validityInMilliseconds = 3600000;

    // 생성자에서 비밀 키 초기화 (application.yml 값 사용)
    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 생성
    public String createToken(String userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(userId)                    // subject로 사용자 Id저장
                .claim("role", role)                   // 사용자 권한 정보 추가
                .setIssuedAt(now)                      // 토큰 발급 시간
                .setExpiration(expiry)                 // 토큰 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256) // 서명 알고리즘과 키 설정
                .compact();
    }

    // 토큰에서 userId 추출
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰에서 role 추출
    public String getRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }
}
