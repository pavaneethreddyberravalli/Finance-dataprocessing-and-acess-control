package com.finance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 80)
    private String username;

    @NotBlank
    @Email
    @Column(unique = true)
    private  String email;
    @NotBlank
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role=Role.VIEWER;

    @Column(name = "is_active", nullable = false)
    private boolean isActive=true;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = true;
    }


}
