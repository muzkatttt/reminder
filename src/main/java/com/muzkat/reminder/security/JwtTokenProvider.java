package com.muzkat.reminder.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

/**
 * Класс-провайдер для создания и валидации JWT-токена.
 * Используется для генерации токена на основе email пользователя и их последующей валидации
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    /**
     * Поле секретный ключ для подписи JWT-токена
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Поле время жизни токена в миллисекундах
     */
    @Value("${jwt.expiration}")
    private long validityInMillis;


    /**
     * Метод инициализирует секретный ключ с помощью Base64
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    /**
     * Метод генерирует JWT-токен для указанного email пользователя
     * @param email почта пользователя (используется как логин)
     * @return сгенерированный JWT-токен
     */
    public String generateToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Метод извлекает email пользователя из JWT-токена.
     * @param token JWT-токен
     * @return email пользователя
     */
    public String getEmailFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    /**
     * Метод проверяет валидность JWT-токена
     * @param token JWT-токен
     * @return true, если токен действителен
     */
    public boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token);
        return true;
    }
}



