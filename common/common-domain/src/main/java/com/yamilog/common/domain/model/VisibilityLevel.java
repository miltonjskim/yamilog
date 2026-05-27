package com.yamilog.common.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VisibilityLevel {

    PUBLIC(0),    // 전체 공개
    MANIA(2),     // Mania 이상만
    EXPERT(3);    // Expert 이상만

    private final int requiredLevel;

    public boolean isAccessibleBy(ManiaLevel userLevel) {
        return userLevel.getValue() >= this.requiredLevel;
    }
}
