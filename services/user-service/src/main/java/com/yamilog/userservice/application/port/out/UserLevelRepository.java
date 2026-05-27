package com.yamilog.userservice.application.port.out;

import com.yamilog.userservice.domain.model.UserLevel;

import java.util.List;
import java.util.Optional;

public interface UserLevelRepository {
    UserLevel save(UserLevel userLevel);
    Optional<UserLevel> findByUserIdAndCategoryId(String userId, String categoryId);
    List<UserLevel> findAllByUserId(String userId);
}
