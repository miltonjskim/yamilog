package com.yamilog.userservice.application.port.out;

import com.yamilog.userservice.domain.event.UserFollowedEvent;
import com.yamilog.userservice.domain.event.UserRegisteredEvent;

public interface UserEventPublisher {
    void publish(UserFollowedEvent event);
    void publish(UserRegisteredEvent event);
}
