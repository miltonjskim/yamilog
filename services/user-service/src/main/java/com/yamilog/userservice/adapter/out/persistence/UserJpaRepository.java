package com.yamilog.userservice.adapter.out.persistence;

import com.yamilog.userservice.domain.model.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByPublicId(UUID publicId);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByProviderTypeAndProviderId(ProviderType providerType, String providerId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
