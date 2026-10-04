package com.krishna.ecommerce.service;

import com.krishna.ecommerce.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user) {

        SecretKey key = getSigningKey();

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + expiration)
                )
                .signWith(key)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {

        String email = extractEmail(token);

        return email.equals(user.getEmail()) && !isTokenExpired(token);
    }

    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {

        Date expiration = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

}