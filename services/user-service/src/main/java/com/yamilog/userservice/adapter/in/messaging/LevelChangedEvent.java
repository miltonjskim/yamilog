package com.yamilog.userservice.adapter.in.messaging;

/**
 * level-engine 에서 발행하는 LevelChanged 이벤트 페이로드.
 * common-domain 에 없는 이유: level-engine 전용 DTO 이므로 adapter 계층에 위치.
 */
public record LevelChangedEvent(
    String eventId,
    String eventType,
    String aggregateId,   // userId
    String categoryId,
    int newLevel,
    int qualityScore,
    int reviewCount
) {}
