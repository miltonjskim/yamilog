package com.yamilog.userservice.application.service;

import com.yamilog.common.domain.exception.BusinessException;
import com.yamilog.userservice.application.port.in.RegisterUserCommand;
import com.yamilog.userservice.application.port.out.UserEventPublisher;
import com.yamilog.userservice.application.port.out.UserLevelRepository;
import com.yamilog.userservice.application.port.out.UserRepository;
import com.yamilog.userservice.domain.exception.UserErrorCode;
import com.yamilog.userservice.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock UserLevelRepository userLevelRepository;
    @Mock UserEventPublisher eventPublisher;

    @InjectMocks UserService userService;

    @BeforeEach
    void setUp() {
        // BCryptPasswordEncoder 주입 (직접 생성)
        userService = new UserService(
            userRepository, userLevelRepository, eventPublisher,
            new BCryptPasswordEncoder()
        );
    }

    @Test
    @DisplayName("로컬 회원가입 시 User 가 저장되고 반환된다")
    void register_local_savesAndReturnsUser() {
        given(userRepository.existsByEmail("test@example.com")).willReturn(false);
        given(userRepository.existsByNickname("테스터")).willReturn(false);
        given(userRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        User result = userService.register(
            RegisterUserCommand.local("test@example.com", "테스터", "password123")
        );

        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getNickname()).isEqualTo("테스터");
        then(eventPublisher).should().publish(any(com.yamilog.userservice.domain.event.UserRegisteredEvent.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 가입 시 EMAIL_ALREADY_EXISTS 예외가 발생한다")
    void register_duplicateEmail_throwsEmailAlreadyExists() {
        given(userRepository.existsByEmail("dup@example.com")).willReturn(true);

        assertThatThrownBy(() ->
            userService.register(RegisterUserCommand.local("dup@example.com", "닉네임", "pass1234"))
        )
            .isInstanceOf(BusinessException.class)
            .extracting(e -> ((BusinessException) e).getErrorCode())
            .isEqualTo(UserErrorCode.EMAIL_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("존재하지 않는 userId 조회 시 USER_NOT_FOUND 예외가 발생한다")
    void getProfile_notFound_throwsUserNotFound() {
        given(userRepository.findById("unknown")).willReturn(java.util.Optional.empty());

        assertThatThrownBy(() ->
            userService.getProfile(new com.yamilog.userservice.application.port.in.GetUserProfileQuery("unknown"))
        )
            .isInstanceOf(BusinessException.class)
            .extracting(e -> ((BusinessException) e).getErrorCode())
            .isEqualTo(UserErrorCode.USER_NOT_FOUND);
    }

    private User makeUser(String userId) {
        return User.builder()
            .userId(userId).nickname("테스터").email("t@t.com")
            .followersCount(0).followingCount(0)
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
            .build();
    }
}
