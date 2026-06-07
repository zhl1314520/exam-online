package com.exam.online.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Integer teacherId, String teacherNo, String realName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("teacherId", teacherId);
        claims.put("teacherNo", teacherNo);
        claims.put("realName", realName);
        claims.put("role", "teacher");

        return Jwts.builder()
                .claims(claims)
                .subject(teacherNo)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Integer getTeacherId(String token) {
        Claims claims = parseToken(token);
        return claims.get("teacherId", Integer.class);
    }

    public String getTeacherNo(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }
}