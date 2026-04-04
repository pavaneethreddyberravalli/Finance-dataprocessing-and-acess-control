package com.finance.controller;

import com.finance.dto.request.ForgotPasswordRequest;
import com.finance.dto.response.ApiResponse;
import com.finance.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String token = forgotPasswordService.generateResetToken(request);
        return ResponseEntity.ok(ApiResponse.ok("Reset link generated.", token));
    }
}
