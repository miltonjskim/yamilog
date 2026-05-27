package com.yamilog.common.infra.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yamilog.common.infra.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties();
        // 최소 32바이트 시크릿 (HS256)
        props.setSecret("test-secret-key-must-be-at-least-32bytes!!");
        props.setAccessTokenExpireMs(3_600_000L);
        jwtProvider = new JwtProvider(props, new ObjectMapper());
    }

    @Test
    @DisplayName("토큰을 생성하면 파싱 시 동일한 userId 가 반환된다")
    void createAndParse_returnsCorrectUserId() {
        UserPrincipal principal = UserPrincipal.builder()
            .userId("usr_abc123")
            .nickname("밀턴")
            .levels(Map.of("coffee", 2, "whiskey", 1))
            .build();

        String token = jwtProvider.createAccessToken(principal);
        UserPrincipal parsed = jwtProvider.parseToken(token);

        assertThat(parsed.getUserId()).isEqualTo("usr_abc123");
        assertThat(parsed.getNickname()).isEqualTo("밀턴");
        assertThat(parsed.getLevels()).containsEntry("coffee", 2);
    }

    @Test
    @DisplayName("유효한 토큰이면 isValid 가 true 를 반환한다")
    void isValid_withValidToken_returnsTrue() {
        UserPrincipal principal = UserPrincipal.builder()
            .userId("usr_001")
            .nickname("테스터")
            .build();

        String token = jwtProvider.createAccessToken(principal);

        assertThat(jwtProvider.isValid(token)).isTrue();
    }

    @Test
    @DisplayName("위조된 토큰이면 isValid 가 false 를 반환한다")
    void isValid_withTamperedToken_returnsFalse() {
        assertThat(jwtProvider.isValid("tampered.jwt.token")).isFalse();
    }
}
