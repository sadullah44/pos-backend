package com.example.pos_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    // Güvenlik anahtarı (Bileti mühürlemek için kullanılır)
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Oturum Süresi: 8 Saat (Milisaniye cinsinden)
    private final long jwtExpirationMs = 8 * 60 * 60 * 1000;

    // TOKEN ÜRETME (Giriş yapıldığında çalışır)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    // TOKEN'DAN KULLANICI ADINI ÇEKME
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // TOKEN GEÇERLİ Mİ?
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}