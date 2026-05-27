package com.yamilog.userservice.domain.event;

import com.yamilog.common.domain.event.DomainEvent;

import java.time.Instant;

public record UserFollowedEvent(
    String eventId,
    String eventType,
    Instant occurredAt,
    String aggregateId,   // followerId
    String followeeId
) implements DomainEvent {

    public static UserFollowedEvent of(String followerId, String followeeId) {
        return new UserFollowedEvent(
            java.util.UUID.randomUUID().toString(),
            "UserFollowed",
            Instant.now(),
            followerId,
            followeeId
        );
    }
}
