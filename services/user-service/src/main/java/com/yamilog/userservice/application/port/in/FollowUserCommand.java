package com.yamilog.userservice.application.port.in;

public record FollowUserCommand(
    String followerId,
    String followeeId
) {}
