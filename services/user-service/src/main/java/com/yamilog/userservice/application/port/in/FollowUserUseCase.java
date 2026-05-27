package com.yamilog.userservice.application.port.in;

public interface FollowUserUseCase {
    void follow(FollowUserCommand command);
    void unfollow(FollowUserCommand command);
}
