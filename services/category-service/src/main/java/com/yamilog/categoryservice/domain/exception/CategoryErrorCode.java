package com.yamilog.categoryservice.domain.exception;

import com.yamilog.common.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {

    CATEGORY_NOT_FOUND("CAT_001", "카테고리를 찾을 수 없습니다.", 404),
    CATEGORY_NAME_DUPLICATE("CAT_002", "이미 존재하는 카테고리 이름입니다.", 409),
    FIELD_KEY_DUPLICATE("CAT_003", "중복된 평가 항목 키가 있습니다.", 400),
    INVALID_FIELD_OPTIONS("CAT_004", "SELECT 타입은 선택지가 필요합니다.", 400);

    private final String code;
    private final String message;
    private final int httpStatus;
}
