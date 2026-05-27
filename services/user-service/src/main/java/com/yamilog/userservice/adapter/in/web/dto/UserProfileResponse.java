package com.yamilog.userservice.adapter.in.web.dto;

import com.yamilog.userservice.domain.model.User;
import com.yamilog.userservice.domain.model.UserLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record UserProfileResponse(
    String userId,
    String nickname,
    String profileImage,
    long followersCount,
    long followingCount,
    Map<String, String> levels,   // categoryId -> ManiaLevel name
    LocalDateTime createdAt
) {
    public static UserProfileResponse of(User user, List<UserLevel> levels) {
        Map<String, String> levelMap = levels.stream()
            .collect(Collectors.toMap(
                UserLevel::getCategoryId,
                l -> l.getManiaLevel().name()
            ));
        return new UserProfileResponse(
            user.getUserId(),
            user.getNickname(),
            user.getProfileImage(),
            user.getFollowersCount(),
            user.getFollowingCount(),
            levelMap,
            user.getCreatedAt()
        );
    }
}
