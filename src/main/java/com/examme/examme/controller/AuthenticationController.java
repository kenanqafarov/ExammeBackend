package com.examme.examme.controller;

import com.examme.examme.dto.*;
import com.examme.examme.enums.UserRole;
import com.examme.examme.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @Operation(summary = "Register as student or teacher")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRegistrationDto registrationDto) {
        userService.registerUser(
                registrationDto.getEmail(),
                registrationDto.getPassword(),
                registrationDto.getFullName(),
                UserRole.valueOf(registrationDto.getRole())
        );
        return ResponseEntity.ok(new ApiResponse("User registered successfully", true));
    }

    @Operation(summary = "Login and receive JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        String token = userService.login(loginRequest);
        UserDto user = userService.getUserByEmail(loginRequest.getEmail());
        LoginResponseDto response = LoginResponseDto.builder()
                .token(token)
                .user(user)
                .message("Login successful")
                .build();
        return ResponseEntity.ok(response);
    }
}
