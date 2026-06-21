package com.example.online.cafe.domain.manager.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    private SecretKey key;

    // Initializes the key after the class is instantiated and the jwtSecret is injected,
    // preventing the repeated creation of the key and enhancing performance
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Get username from JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        /*
        // Following code only works on older version of JJWT before 0.12.0. Configure implementation if this code is needed to be used
        // Some of the functions below (setSigningKey, parseClaimsJws, getBody) are replaced with other functions in newer version
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
         */
    }

    // Validate JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token);
            return true;

            /*
            // Following code only works on older version of JJWT before 0.12.0. Configure implementation if this code is needed to be used
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
            */
        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature(유효하지 않은 JWT 시그니처 입니다): " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token(유효하지 않는 JWT 토큰 입니다): " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired(JWT 토큰이 만료 되었습니다): " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported(JWT 토큰이 지원되지 않습니다): " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty(JWT 클레임(속성 정보) 스트링이 비어 있습니다): " + e.getMessage());
        }

        return false;
    }
}
