package com.yamilog.userservice.adapter.in.messaging;

import com.yamilog.common.domain.model.ManiaLevel;
import com.yamilog.common.infra.kafka.KafkaTopics;
import com.yamilog.userservice.application.port.out.UserLevelRepository;
import com.yamilog.userservice.domain.model.UserLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LevelChangedEventListener {

    private final UserLevelRepository userLevelRepository;

    @KafkaListener(topics = KafkaTopics.LEVEL_CHANGED, groupId = "user-service")
    public void onLevelChanged(LevelChangedEvent event) {
        log.info("LevelChanged 수신: userId={}, category={}, newLevel={}",
            event.aggregateId(), event.categoryId(), event.newLevel());

        UserLevel updatedLevel = UserLevel.builder()
            .userId(event.aggregateId())
            .categoryId(event.categoryId())
            .maniaLevel(ManiaLevel.fromValue(event.newLevel()))
            .qualityScore(event.qualityScore())
            .reviewCount(event.reviewCount())
            .updatedAt(LocalDateTime.now())
            .build();

        userLevelRepository.save(updatedLevel);
    }
}
