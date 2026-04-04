package com.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class AuthResponse {
    private String  accessToken;
    private String  refreshToken;
    private String  tokenType = "Bearer";
    private String  email;
    private Long userId;
    private String  name;
    private String  role;

    //public AuthResponse() {}
    public AuthResponse(String accessToken,
                        String refreshToken,
                        Long userId,
                        String email,
                        String name,
                        String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }


}
