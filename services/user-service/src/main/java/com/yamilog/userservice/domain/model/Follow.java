package com.yamilog.userservice.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Follow {
    String followerId;
    String followeeId;
    LocalDateTime createdAt;
}
