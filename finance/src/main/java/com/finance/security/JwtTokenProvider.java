package com.finance.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.expiration}")
    private long accessTokenExpiration;

    /**
     * Derives a signing key from the configured Base64-encoded secret.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a signed JWT access token embedding the user's email and role.
     */
    public String generateAccessToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return buildToken(userPrincipal.getUsername(),
                userPrincipal.getRole(),
                accessTokenExpiration);
    }

    /**
     * Overload — generate from UserDetailsImpl directly (used after refresh).
     */
    public String generateAccessTokenFromUser(UserDetailsImpl userDetails) {
        return buildToken(userDetails.getUsername(),
                userDetails.getRole(),
                accessTokenExpiration);
    }

    private String buildToken(String subject, String role, long expiryMs) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + expiryMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the email (subject) from a validated JWT.
     */
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extracts the role claim from a validated JWT.
     */
    public String getRoleFromToken(String token) {
        return (String) parseClaims(token).get("role");
    }

    /**
     * Returns true if the token is structurally valid, signed correctly,
     * and has not expired.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


