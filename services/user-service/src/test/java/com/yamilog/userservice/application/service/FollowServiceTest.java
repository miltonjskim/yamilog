package com.yamilog.userservice.application.service;

import com.yamilog.common.domain.exception.BusinessException;
import com.yamilog.userservice.application.port.in.FollowUserCommand;
import com.yamilog.userservice.application.port.out.FollowRepository;
import com.yamilog.userservice.application.port.out.UserEventPublisher;
import com.yamilog.userservice.application.port.out.UserRepository;
import com.yamilog.userservice.domain.exception.UserErrorCode;
import com.yamilog.userservice.domain.model.Follow;
import com.yamilog.userservice.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock FollowRepository followRepository;
    @Mock UserRepository userRepository;
    @Mock UserEventPublisher eventPublisher;

    @InjectMocks FollowService followService;

    @Test
    @DisplayName("팔로우 성공 시 Follow 가 저장되고 이벤트가 발행된다")
    void follow_success_savesFollowAndPublishesEvent() {
        given(followRepository.exists("u1", "u2")).willReturn(false);
        given(userRepository.findById("u2")).willReturn(Optional.of(makeUser("u2")));
        given(followRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        followService.follow(new FollowUserCommand("u1", "u2"));

        then(followRepository).should().save(any());
        then(eventPublisher).should().publish(any(com.yamilog.userservice.domain.event.UserFollowedEvent.class));
    }

    @Test
    @DisplayName("자기 자신을 팔로우하면 CANNOT_FOLLOW_SELF 예외가 발생한다")
    void follow_self_throwsCannotFollowSelf() {
        assertThatThrownBy(() ->
            followService.follow(new FollowUserCommand("u1", "u1"))
        )
            .isInstanceOf(BusinessException.class)
            .extracting(e -> ((BusinessException) e).getErrorCode())
            .isEqualTo(UserErrorCode.CANNOT_FOLLOW_SELF);
    }

    @Test
    @DisplayName("이미 팔로우한 사용자를 다시 팔로우하면 ALREADY_FOLLOWING 예외가 발생한다")
    void follow_alreadyFollowing_throwsAlreadyFollowing() {
        given(followRepository.exists("u1", "u2")).willReturn(true);

        assertThatThrownBy(() ->
            followService.follow(new FollowUserCommand("u1", "u2"))
        )
            .isInstanceOf(BusinessException.class)
            .extracting(e -> ((BusinessException) e).getErrorCode())
            .isEqualTo(UserErrorCode.ALREADY_FOLLOWING);
    }

    @Test
    @DisplayName("팔로우하지 않은 사용자를 언팔로우하면 NOT_FOLLOWING 예외가 발생한다")
    void unfollow_notFollowing_throwsNotFollowing() {
        given(followRepository.exists("u1", "u2")).willReturn(false);

        assertThatThrownBy(() ->
            followService.unfollow(new FollowUserCommand("u1", "u2"))
        )
            .isInstanceOf(BusinessException.class)
            .extracting(e -> ((BusinessException) e).getErrorCode())
            .isEqualTo(UserErrorCode.NOT_FOLLOWING);
    }

    private User makeUser(String userId) {
        return User.builder()
            .userId(userId).nickname("U").email("u@u.com")
            .followersCount(0).followingCount(0)
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
            .build();
    }
}
