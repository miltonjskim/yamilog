package com.yamilog.common.infra.security.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yamilog.common.infra.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties properties;
    private final ObjectMapper objectMapper;

    public String createAccessToken(UserPrincipal principal) {
        return Jwts.builder()
            .subject(principal.getUserId())
            .claim("nickname", principal.getNickname())
            .claim("levels", principal.getLevels())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + properties.getAccessTokenExpireMs()))
            .signWith(signingKey())
            .compact();
    }

    public UserPrincipal parseToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(signingKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

        Map<String, Integer> levels = objectMapper.convertValue(
            claims.get("levels"),
            new TypeReference<>() {}
        );

        return UserPrincipal.builder()
            .userId(claims.getSubject())
            .nickname(claims.get("nickname", String.class))
            .levels(levels)
            .build();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
