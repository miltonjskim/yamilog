package com.yamilog.userservice.adapter.out.persistence;

import com.yamilog.userservice.application.port.out.UserLevelRepository;
import com.yamilog.userservice.domain.model.UserLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class UserLevelPersistenceAdapter implements UserLevelRepository {

    private final UserLevelJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public UserLevel save(UserLevel userLevel) {
        UserLevelEntity entity = jpaRepository
            .findByUserIdAndCategoryId(userLevel.getUserId(), userLevel.getCategoryId())
            .map(existing -> {
                existing.setManiaLevel(userLevel.getManiaLevel().getValue());
                existing.setQualityScore(userLevel.getQualityScore());
                existing.setReviewCount(userLevel.getReviewCount());
                return existing;
            })
            .orElseGet(() -> mapper.levelToEntity(userLevel));
        return mapper.levelToDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<UserLevel> findByUserIdAndCategoryId(String userId, String categoryId) {
        return jpaRepository.findByUserIdAndCategoryId(userId, categoryId)
            .map(mapper::levelToDomain);
    }

    @Override
    public List<UserLevel> findAllByUserId(String userId) {
        return jpaRepository.findAllByUserId(userId).stream()
            .map(mapper::levelToDomain)
            .toList();
    }
}
