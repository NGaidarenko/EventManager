package com.example.eventmanager.jwt;

import com.example.eventmanager.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenManager {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenManager.class);
    private final Long tokenLifeTime;
    private final Key signKey;

    public JwtTokenManager(@Value("${jwt.lifetime}") Long tokenLifeTime,
                           @Value("${jwt.key}") String signKey) {
        this.tokenLifeTime = tokenLifeTime;
        this.signKey = new SecretKeySpec(signKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }


    public String generateToken(User user) {
        log.info("Generating JWT token");
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.role().name());
        Date issuedTime = new Date();
        Date expiredTime = new Date(issuedTime.getTime() + tokenLifeTime);

        return Jwts.builder()
                .claims(claims)
                .subject(user.login())
                .issuedAt(issuedTime)
                .expiration(expiredTime)
                .signWith(signKey)
                .compact();
    }

    public boolean validateToken(String token) {
        log.info("Validating JWT token");
        try {
            Jwts.parser()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public String getLoginFromToken(String token) {
        log.info("Getting login from token");
        return Jwts.parser()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        log.info("Getting role from token");
        return Jwts.parser()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
