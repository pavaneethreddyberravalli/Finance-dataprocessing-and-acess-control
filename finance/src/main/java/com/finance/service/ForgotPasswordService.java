package com.finance.service;

import com.finance.dto.request.ForgotPasswordRequest;
import com.finance.entity.User;
import com.finance.excption.UserNotFoundException;
import com.finance.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;

    @Transactional
    public String generateResetToken(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"+ request.getEmail()));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(30));

        userRepository.saveAndFlush(user);

        return token;
    }
}
