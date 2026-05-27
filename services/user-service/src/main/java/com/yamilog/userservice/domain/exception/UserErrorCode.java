package com.yamilog.userservice.domain.exception;

import com.yamilog.common.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다.", 404),
    EMAIL_ALREADY_EXISTS("USER_002", "이미 사용 중인 이메일입니다.", 409),
    NICKNAME_ALREADY_EXISTS("USER_003", "이미 사용 중인 닉네임입니다.", 409),
    CANNOT_FOLLOW_SELF("USER_004", "자기 자신을 팔로우할 수 없습니다.", 400),
    ALREADY_FOLLOWING("USER_005", "이미 팔로우한 사용자입니다.", 409),
    NOT_FOLLOWING("USER_006", "팔로우하지 않은 사용자입니다.", 400);

    private final String code;
    private final String message;
    private final int httpStatus;
}
