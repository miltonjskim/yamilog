package com.yamilog.userservice.application.port.out;

import com.yamilog.userservice.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderTypeAndProviderId(
        com.yamilog.userservice.domain.model.ProviderType providerType, String providerId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
