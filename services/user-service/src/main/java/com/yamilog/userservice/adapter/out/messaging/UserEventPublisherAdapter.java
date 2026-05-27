package com.yamilog.userservice.adapter.out.messaging;

import com.yamilog.common.infra.kafka.KafkaTopics;
import com.yamilog.userservice.application.port.out.UserEventPublisher;
import com.yamilog.userservice.domain.event.UserFollowedEvent;
import com.yamilog.userservice.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventPublisherAdapter implements UserEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(UserFollowedEvent event) {
        kafkaTemplate.send(KafkaTopics.USER_FOLLOWED, event.aggregateId(), event);
        log.info("UserFollowedEvent 발행: followerId={}, followeeId={}",
            event.aggregateId(), event.followeeId());
    }

    @Override
    public void publish(UserRegisteredEvent event) {
        // 현재 구독자 없음 — 필요 시 토픽 추가
        log.info("UserRegisteredEvent 발행: userId={}", event.aggregateId());
    }
}
