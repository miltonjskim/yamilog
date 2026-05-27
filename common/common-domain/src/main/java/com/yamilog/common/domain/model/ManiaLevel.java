package com.yamilog.common.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum ManiaLevel {

    NEWBIE(0),
    ENTHUSIAST(1),
    MANIA(2),
    EXPERT(3),
    MASTER(4);

    private final int value;

    // 퀄리티 점수 임계값 (레벨 승급 조건)
    public static final Map<ManiaLevel, Integer> QUALITY_THRESHOLD = Map.of(
        ENTHUSIAST, 60,
        MANIA,      75,
        EXPERT,     85,
        MASTER,     90
    );

    // 리뷰 수 임계값 (레벨 승급 조건)
    public static final Map<ManiaLevel, Integer> REVIEW_THRESHOLD = Map.of(
        ENTHUSIAST,  5,
        MANIA,      20,
        EXPERT,     50,
        MASTER,    100
    );

    public static ManiaLevel fromValue(int value) {
        for (ManiaLevel level : values()) {
            if (level.value == value) return level;
        }
        return NEWBIE;
    }

    public boolean isAtLeast(ManiaLevel required) {
        return this.value >= required.value;
    }

    public ManiaLevel downgrade() {
        if (this == NEWBIE) return NEWBIE;
        return fromValue(this.value - 1);
    }
}
