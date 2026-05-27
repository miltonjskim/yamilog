package com.yamilog.userservice.application.service;

import com.yamilog.common.domain.exception.BusinessException;
import com.yamilog.userservice.application.port.in.FollowUserCommand;
import com.yamilog.userservice.application.port.in.FollowUserUseCase;
import com.yamilog.userservice.application.port.out.FollowRepository;
import com.yamilog.userservice.application.port.out.UserEventPublisher;
import com.yamilog.userservice.application.port.out.UserRepository;
import com.yamilog.userservice.domain.event.UserFollowedEvent;
import com.yamilog.userservice.domain.exception.UserErrorCode;
import com.yamilog.userservice.domain.model.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowUserUseCase {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserEventPublisher eventPublisher;

    @Override
    @Transactional
    public void follow(FollowUserCommand command) {
        if (command.followerId().equals(command.followeeId())) {
            throw new BusinessException(UserErrorCode.CANNOT_FOLLOW_SELF);
        }
        if (followRepository.exists(command.followerId(), command.followeeId())) {
            throw new BusinessException(UserErrorCode.ALREADY_FOLLOWING);
        }
        // followee 존재 확인
        userRepository.findById(command.followeeId())
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        followRepository.save(Follow.builder()
            .followerId(command.followerId())
            .followeeId(command.followeeId())
            .createdAt(LocalDateTime.now())
            .build());

        eventPublisher.publish(UserFollowedEvent.of(command.followerId(), command.followeeId()));
    }

    @Override
    @Transactional
    public void unfollow(FollowUserCommand command) {
        if (!followRepository.exists(command.followerId(), command.followeeId())) {
            throw new BusinessException(UserErrorCode.NOT_FOLLOWING);
        }
        followRepository.delete(command.followerId(), command.followeeId());
    }
}
