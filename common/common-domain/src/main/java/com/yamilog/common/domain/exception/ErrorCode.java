package com.yamilog.common.domain.exception;

public interface ErrorCode {
    String getCode();
    String getMessage();
    int getHttpStatus();
}
