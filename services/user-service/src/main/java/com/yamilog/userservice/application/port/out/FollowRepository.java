package com.yamilog.userservice.application.port.out;

import com.yamilog.userservice.domain.model.Follow;

import java.util.Optional;

public interface FollowRepository {
    Follow save(Follow follow);
    void delete(String followerId, String followeeId);
    Optional<Follow> find(String followerId, String followeeId);
    boolean exists(String followerId, String followeeId);
    long countFollowers(String userId);
    long countFollowing(String userId);
}
