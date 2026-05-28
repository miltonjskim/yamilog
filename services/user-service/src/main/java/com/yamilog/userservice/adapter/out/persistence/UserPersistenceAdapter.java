package com.yamilog.userservice.adapter.out.persistence;

import com.yamilog.userservice.application.port.out.UserRepository;
import com.yamilog.userservice.domain.model.ProviderType;
import com.yamilog.userservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class UserPersistenceAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = jpaRepository.findByPublicId(UUID.fromString(user.getUserId()))
            .map(existing -> {
                existing.setNickname(user.getNickname());
                existing.setPasswordHash(user.getPasswordHash());
                existing.setProfileImage(user.getProfileImage());
                existing.setFollowersCount(user.getFollowersCount());
                existing.setFollowingCount(user.getFollowingCount());
                return existing;
            })
            .orElseGet(() -> mapper.toEntity(user));
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<User> findById(String userId) {
        return jpaRepository.findByPublicId(UUID.fromString(userId)).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByProviderTypeAndProviderId(ProviderType providerType, String providerId) {
        return jpaRepository.findByProviderTypeAndProviderId(providerType, providerId)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return jpaRepository.existsByNickname(nickname);
    }
}
