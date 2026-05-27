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
        return mapper.levelToDomain(jpaRepository.save(mapper.levelToEntity(userLevel)));
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
