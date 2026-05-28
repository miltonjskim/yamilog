package com.yamilog.categoryservice.adapter.out.messaging;

import com.yamilog.categoryservice.application.port.out.CategoryEventPublisher;
import com.yamilog.categoryservice.domain.event.CategorySchemaUpdatedEvent;
import com.yamilog.common.infra.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryEventPublisherAdapter implements CategoryEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(CategorySchemaUpdatedEvent event) {
        kafkaTemplate.send(KafkaTopics.CATEGORY_SCHEMA_UPDATED, event.aggregateId(), event);
        log.info("CategorySchemaUpdatedEvent 발행: categoryId={}", event.aggregateId());
    }
}
