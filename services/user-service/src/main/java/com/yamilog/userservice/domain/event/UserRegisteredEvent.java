package com.yamilog.userservice.domain.event;

import com.yamilog.common.domain.event.DomainEvent;

import java.time.Instant;

public record UserRegisteredEvent(
    String eventId,
    String eventType,
    Instant occurredAt,
    String aggregateId,   // userId
    String email,
    String nickname
) implements DomainEvent {

    public static UserRegisteredEvent of(String userId, String email, String nickname) {
        return new UserRegisteredEvent(
            java.util.UUID.randomUUID().toString(),
            "UserRegistered",
            Instant.now(),
            userId,
            email,
            nickname
        );
    }
}
