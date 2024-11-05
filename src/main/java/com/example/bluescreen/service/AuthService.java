package com.example.bluescreen.service;

import com.example.bluescreen.dto.LoginRequest;
import com.example.bluescreen.dto.LoginResponse;
import com.example.bluescreen.model.User;
import com.example.bluescreen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        // 사용자 찾기
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new BadCredentialsException("잘못된 로그인 정보입니다"));

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 로그인 정보입니다");
        }

        // JWT 토큰 생성
        String token = jwtService.generateToken(user.getUsername());

        // 로그인 응답 생성
        return LoginResponse.builder()
            .username(user.getUsername())
            .token(token)
            .message("로그인 성공")
            .build();
    }
} 