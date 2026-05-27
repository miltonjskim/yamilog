package com.yamilog.common.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR("COMMON_001", "서버 내부 오류가 발생했습니다.", 500),
    INVALID_INPUT("COMMON_002", "잘못된 입력값입니다.", 400),
    UNAUTHORIZED("COMMON_003", "인증이 필요합니다.", 401),
    FORBIDDEN("COMMON_004", "접근 권한이 없습니다.", 403),
    NOT_FOUND("COMMON_005", "요청한 리소스를 찾을 수 없습니다.", 404),
    LEVEL_INSUFFICIENT("COMMON_006", "이 기능을 사용하기 위한 레벨이 부족합니다.", 403);

    private final String code;
    private final String message;
    private final int httpStatus;
}
