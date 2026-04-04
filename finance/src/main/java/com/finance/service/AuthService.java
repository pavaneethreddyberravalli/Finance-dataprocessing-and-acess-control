package com.finance.service;

import com.finance.dto.request.LoginRequest;
import com.finance.dto.request.RegisterRequest;
import com.finance.dto.response.AuthResponse;
import com.finance.entity.RefreshToken;
import com.finance.entity.User;
import com.finance.excption.AuthenticationFailedException;
import com.finance.excption.ResourceAlreadyExitsException;
import com.finance.excption.TokenRefreshException;
import com.finance.repository.UserRepository;
import com.finance.security.JwtTokenProvider;
import com.finance.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;


    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExitsException(
                    "Email already in use: " + request.getEmail());
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole()
        );

        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails =
                    (UserDetailsImpl) authentication.getPrincipal();

            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            RefreshToken refreshToken =
                    refreshTokenService.createRefreshToken(userDetails.getId());

            return new AuthResponse(
                    accessToken,
                    refreshToken.getToken(),
                    userDetails.getId(),
                    userDetails.getEmail(),
                    userDetails.getUsername(),
                    userDetails.getRole()
            );


        }catch (Exception e){
            throw new AuthenticationFailedException("Invalid email or password");

        }
    }



    public AuthResponse refresh(String requestRefreshToken) {
        RefreshToken refreshToken =
                refreshTokenService.findByToken(requestRefreshToken);
        if (refreshToken == null) {
            throw new TokenRefreshException("Refresh token not found");
        }

        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        String newAccessToken =
                jwtTokenProvider.generateAccessTokenFromUser(userDetails);

        return new AuthResponse(
                newAccessToken,
                requestRefreshToken,
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
        );
    }
    @Transactional
    public void logout(User user) {
        //refreshTokenService.deleteByUser(user);
        refreshTokenService.deleteByUser(user.getId());
        SecurityContextHolder.clearContext();
    }
}