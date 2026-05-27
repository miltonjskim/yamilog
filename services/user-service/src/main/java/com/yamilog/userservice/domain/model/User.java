package com.yamilog.userservice.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class User {
    String userId;
    String nickname;
    String email;
    String passwordHash;      // OAuth2 사용자는 null
    String profileImage;
    ProviderType providerType;
    String providerId;        // 소셜 로그인 제공자 내부 ID
    long followersCount;
    long followingCount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
