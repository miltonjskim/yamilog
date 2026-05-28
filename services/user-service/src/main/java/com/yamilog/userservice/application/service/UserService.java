package com.yamilog.userservice.application.service;

import com.yamilog.common.domain.exception.BusinessException;
import com.yamilog.userservice.application.port.in.GetUserProfileQuery;
import com.yamilog.userservice.application.port.in.GetUserProfileUseCase;
import com.yamilog.userservice.application.port.in.RegisterUserCommand;
import com.yamilog.userservice.application.port.in.RegisterUserUseCase;
import com.yamilog.userservice.application.port.out.UserEventPublisher;
import com.yamilog.userservice.application.port.out.UserLevelRepository;
import com.yamilog.userservice.application.port.out.UserRepository;
import com.yamilog.userservice.domain.event.UserRegisteredEvent;
import com.yamilog.userservice.domain.exception.UserErrorCode;
import com.yamilog.userservice.domain.model.ProviderType;
import com.yamilog.userservice.domain.model.User;
import com.yamilog.userservice.domain.model.UserLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, GetUserProfileUseCase {

    private final UserRepository userRepository;
    private final UserLevelRepository userLevelRepository;
    private final UserEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new BusinessException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByNickname(command.nickname())) {
            throw new BusinessException(UserErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        String passwordHash = command.providerType() == ProviderType.LOCAL
            ? passwordEncoder.encode(command.password())
            : null;

        User user = User.builder()
            .userId(UUID.randomUUID().toString())
            .email(command.email())
            .nickname(command.nickname())
            .passwordHash(passwordHash)
            .providerType(command.providerType())
            .providerId(command.providerId())
            .followersCount(0)
            .followingCount(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        User saved = userRepository.save(user);
        eventPublisher.publish(UserRegisteredEvent.of(saved.getUserId(), saved.getEmail(), saved.getNickname()));
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public User getProfile(GetUserProfileQuery query) {
        return userRepository.findById(query.userId())
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLevel> getLevels(GetUserProfileQuery query) {
        return userLevelRepository.findAllByUserId(query.userId());
    }
}
