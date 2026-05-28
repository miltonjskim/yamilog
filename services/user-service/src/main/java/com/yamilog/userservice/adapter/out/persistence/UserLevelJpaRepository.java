package com.yamilog.userservice.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface UserLevelJpaRepository extends JpaRepository<UserLevelEntity, Long> {
    Optional<UserLevelEntity> findByUserIdAndCategoryId(String userId, String categoryId);
    List<UserLevelEntity> findAllByUserId(String userId);
}
