package com.yamilog.common.infra.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenExpireMs = 3_600_000L;   // 1시간
    private long refreshTokenExpireMs = 604_800_000L; // 7일
}
