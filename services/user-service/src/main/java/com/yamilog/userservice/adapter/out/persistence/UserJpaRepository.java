package com.yamilog.userservice.adapter.out.persistence;

import com.yamilog.userservice.domain.model.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByProviderTypeAndProviderId(ProviderType providerType, String providerId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
