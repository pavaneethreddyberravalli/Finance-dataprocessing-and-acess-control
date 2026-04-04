package com.finance.security;

import com.finance.entity.Role;
import com.finance.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String role;
    private boolean isActive;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String password,
                           String role, boolean isActive,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                user.isActive(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }
    public String getRole() {
        return role;
    }

    @Override public String getUsername() {
        return email;
    }
    @Override public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override public boolean isAccountNonExpired() {
        return true;
    }
    @Override public boolean isAccountNonLocked() {
        return isActive;
    }
    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override public boolean isEnabled() {
        return isActive;
    }
}


