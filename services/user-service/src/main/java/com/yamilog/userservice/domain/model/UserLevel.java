package com.yamilog.userservice.domain.model;

import com.yamilog.common.domain.model.ManiaLevel;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class UserLevel {
    String userId;
    String categoryId;
    ManiaLevel maniaLevel;
    int qualityScore;
    int reviewCount;
    LocalDateTime updatedAt;
}
