package com.yamilog.common.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VisibilityLevelTest {

    @Test
    @DisplayName("PUBLIC 리뷰는 NEWBIE 도 접근할 수 있다")
    void public_isAccessibleByNewbie() {
        assertThat(VisibilityLevel.PUBLIC.isAccessibleBy(ManiaLevel.NEWBIE)).isTrue();
    }

    @Test
    @DisplayName("MANIA 공개 리뷰는 ENTHUSIAST 가 접근할 수 없다")
    void mania_isNotAccessibleByEnthusiast() {
        assertThat(VisibilityLevel.MANIA.isAccessibleBy(ManiaLevel.ENTHUSIAST)).isFalse();
    }

    @Test
    @DisplayName("MANIA 공개 리뷰는 MANIA 이상이면 접근 가능하다")
    void mania_isAccessibleByManiaAndAbove() {
        assertThat(VisibilityLevel.MANIA.isAccessibleBy(ManiaLevel.MANIA)).isTrue();
        assertThat(VisibilityLevel.MANIA.isAccessibleBy(ManiaLevel.MASTER)).isTrue();
    }

    @Test
    @DisplayName("EXPERT 공개 리뷰는 EXPERT 이상만 접근 가능하다")
    void expert_isAccessibleByExpertAndAbove() {
        assertThat(VisibilityLevel.EXPERT.isAccessibleBy(ManiaLevel.MANIA)).isFalse();
        assertThat(VisibilityLevel.EXPERT.isAccessibleBy(ManiaLevel.EXPERT)).isTrue();
        assertThat(VisibilityLevel.EXPERT.isAccessibleBy(ManiaLevel.MASTER)).isTrue();
    }
}
