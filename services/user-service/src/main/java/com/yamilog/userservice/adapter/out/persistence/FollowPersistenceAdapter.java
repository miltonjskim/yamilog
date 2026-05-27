package com.yamilog.userservice.adapter.out.persistence;

import com.yamilog.userservice.application.port.out.FollowRepository;
import com.yamilog.userservice.domain.model.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class FollowPersistenceAdapter implements FollowRepository {

    private final FollowJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public Follow save(Follow follow) {
        return mapper.followToDomain(jpaRepository.save(mapper.followToEntity(follow)));
    }

    @Override
    public void delete(String followerId, String followeeId) {
        jpaRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Override
    public Optional<Follow> find(String followerId, String followeeId) {
        return jpaRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
            .map(mapper::followToDomain);
    }

    @Override
    public boolean exists(String followerId, String followeeId) {
        return jpaRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Override
    public long countFollowers(String userId) {
        return jpaRepository.countByFolloweeId(userId);
    }

    @Override
    public long countFollowing(String userId) {
        return jpaRepository.countByFollowerId(userId);
    }
}
