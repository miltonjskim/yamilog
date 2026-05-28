package com.yamilog.categoryservice.domain.event;

import com.yamilog.common.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CategorySchemaUpdatedEvent(
    String eventId,
    String eventType,
    Instant occurredAt,
    String aggregateId   // categoryId
) implements DomainEvent {

    public static CategorySchemaUpdatedEvent of(String categoryId) {
        return new CategorySchemaUpdatedEvent(
            UUID.randomUUID().toString(),
            "CategorySchemaUpdated",
            Instant.now(),
            categoryId
        );
    }
}
