package com.finance.service;



import com.finance.entity.RefreshToken;
import com.finance.entity.User;
import com.finance.excption.TokenNotFoundException;
import com.finance.excption.TokenRefreshException;
import com.finance.excption.UserNotFoundException;
import com.finance.repository.RefreshTokenRepository;
import com.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

    @Service
    public class RefreshTokenService {

        @Value("${spring.jwt.refresh-expiration}")
        private long refreshTokenExpiration;

        @Autowired private RefreshTokenRepository refreshTokenRepository;
        @Autowired private UserRepository userRepository;

        @Transactional
        public RefreshToken createRefreshToken(Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Revoke any existing refresh token for this user
           // refreshTokenRepository.deleteByUser(user);
            refreshTokenRepository.deleteByUser_Id(userId);


            RefreshToken token = new RefreshToken();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(
                    Instant.now().plusMillis(refreshTokenExpiration));

            return refreshTokenRepository.save(token);
        }


        public RefreshToken findByToken(String token) {
            return refreshTokenRepository.findByToken(token)
                    .orElseThrow(() -> new TokenNotFoundException(
                    "token not found"));
        }


        public RefreshToken verifyExpiration(RefreshToken token) {
            if (token.getExpiryDate().isBefore(Instant.now())) {
                refreshTokenRepository.delete(token);
                throw new TokenRefreshException(
                "refresh token has expired");
            }
            return token;
        }

        public void deleteByUser(Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

           // refreshTokenRepository.deleteByUser(user);
            refreshTokenRepository.deleteByUser_Id(userId);

        }
    }



