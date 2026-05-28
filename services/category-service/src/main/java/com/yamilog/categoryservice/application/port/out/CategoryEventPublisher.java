package com.yamilog.categoryservice.application.port.out;

import com.yamilog.categoryservice.domain.event.CategorySchemaUpdatedEvent;

public interface CategoryEventPublisher {
    void publish(CategorySchemaUpdatedEvent event);
}
