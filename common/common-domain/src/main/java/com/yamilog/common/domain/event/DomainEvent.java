package com.yamilog.common.domain.event;

import java.time.Instant;

public interface DomainEvent {
    String eventId();
    String eventType();
    Instant occurredAt();
    String aggregateId();
}
