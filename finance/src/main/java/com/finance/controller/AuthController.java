package com.finance.controller;

import com.finance.dto.request.LoginRequest;
import com.finance.dto.request.RefreshTokenRequest;
import com.finance.dto.request.RegisterRequest;
import com.finance.dto.response.ApiResponse;
import com.finance.dto.response.AuthResponse;
import com.finance.entity.User;
import com.finance.security.UserDetailsImpl;
import com.finance.service.AuthService;
import com.finance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User registered successfully.", user));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", response));    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        AuthResponse response = authService.refresh(request.getRefreshToken());
        //return ResponseEntity.ok(ApiResponse.ok(response));
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Token refreshed", response));
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        User user = userService.getEntityById(currentUser.getId());
        authService.logout(user);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Logged out successfully.", null));
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getMe(
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        User user = userService.getEntityById(currentUser.getId());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "User details", user));
    }
}


