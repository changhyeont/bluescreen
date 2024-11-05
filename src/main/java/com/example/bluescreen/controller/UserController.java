package com.example.bluescreen.controller;

import com.example.bluescreen.dto.UserResponse;
import com.example.bluescreen.model.User;
import com.example.bluescreen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다. 아이디는 'OO학과-이름' 형식이어야 합니다.")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.findByUsername(userDetails.getUsername());
        
        return ResponseEntity.ok(UserResponse.builder()
            .username(user.getUsername())
            .build());
    }
}