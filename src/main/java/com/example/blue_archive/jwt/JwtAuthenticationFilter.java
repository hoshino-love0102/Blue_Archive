package com.example.blue_archive.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// JWT 인증 필터 (요청마다 한 번 실행된다)
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 관련 기능 제공 클래스
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 현재 요청 URI
        String path = request.getRequestURI();

        // 로그인, 회원가입은 JWT 검사 없이 통과
        if (path.equals("/api/login") || path.equals("/api/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더 가져옴
        String authHeader = request.getHeader("Authorization");

        // Bearer 토큰이 존재할때
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // "Bearer " 이후의 토큰 값만 잘라냄
            String token = authHeader.substring(7);

            // 토큰에서 userId랑 role 추출
            String userId = jwtTokenProvider.getUserId(token);
            String role = jwtTokenProvider.getRole(token);

            // userId랑 role이 유효할 때만 인증 객체 생성
            if (userId != null && role != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId, // principal
                                null,   // credentials (비밀번호 X)
                                List.of(new SimpleGrantedAuthority(role)) // 권한 목록
                        );

                // 인증 객체에 요청 정보 추가
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContextHolder에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
