package com.skillxpress.skillxpress_api.security;

import com.skillxpress.skillxpress_api.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private JwtParser parser() {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build();
    }

    /* ------------ generate token ------------------- */
    public String generateToken(User user) {
        Instant now   = Instant.now();
        Instant   exp   = now.plus(7, ChronoUnit.DAYS);   // 1-week validity

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                /* 👇  add the role here */
                .claim("role", user.getRole().name())     // "ADMIN", "PARENT" …
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    /* ------------ extract helpers ------------------ */
    public String extractUsername(String token) {
        return parser().parseSignedClaims(token).getPayload().getSubject();
    }
    public String extractRole(String token) {
        return parser().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    public boolean isTokenValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername())
                && parser().parseSignedClaims(token).getPayload().getExpiration().after(new Date());
    }

}
