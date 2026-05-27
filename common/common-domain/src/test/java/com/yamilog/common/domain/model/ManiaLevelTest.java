package com.yamilog.common.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ManiaLevelTest {

    @Test
    @DisplayName("value 로 ManiaLevel 을 조회할 수 있다")
    void fromValue_returnsCorrectLevel() {
        assertThat(ManiaLevel.fromValue(0)).isEqualTo(ManiaLevel.NEWBIE);
        assertThat(ManiaLevel.fromValue(2)).isEqualTo(ManiaLevel.MANIA);
        assertThat(ManiaLevel.fromValue(4)).isEqualTo(ManiaLevel.MASTER);
    }

    @Test
    @DisplayName("존재하지 않는 value 는 NEWBIE 를 반환한다")
    void fromValue_unknownValue_returnsNewbie() {
        assertThat(ManiaLevel.fromValue(99)).isEqualTo(ManiaLevel.NEWBIE);
        assertThat(ManiaLevel.fromValue(-1)).isEqualTo(ManiaLevel.NEWBIE);
    }

    @Test
    @DisplayName("isAtLeast 는 동일 레벨이면 true 를 반환한다")
    void isAtLeast_sameLevel_returnsTrue() {
        assertThat(ManiaLevel.MANIA.isAtLeast(ManiaLevel.MANIA)).isTrue();
    }

    @Test
    @DisplayName("isAtLeast 는 낮은 레벨이면 false 를 반환한다")
    void isAtLeast_lowerLevel_returnsFalse() {
        assertThat(ManiaLevel.ENTHUSIAST.isAtLeast(ManiaLevel.MANIA)).isFalse();
    }

    @Test
    @DisplayName("NEWBIE 를 강등하면 NEWBIE 를 유지한다")
    void downgrade_newbie_staysNewbie() {
        assertThat(ManiaLevel.NEWBIE.downgrade()).isEqualTo(ManiaLevel.NEWBIE);
    }

    @Test
    @DisplayName("MASTER 를 강등하면 EXPERT 가 된다")
    void downgrade_master_becomesExpert() {
        assertThat(ManiaLevel.MASTER.downgrade()).isEqualTo(ManiaLevel.EXPERT);
    }

    @Test
    @DisplayName("QUALITY_THRESHOLD 에 NEWBIE 항목이 없다")
    void qualityThreshold_doesNotContainNewbie() {
        assertThat(ManiaLevel.QUALITY_THRESHOLD).doesNotContainKey(ManiaLevel.NEWBIE);
    }

    @Test
    @DisplayName("REVIEW_THRESHOLD 의 MASTER 기준은 100 이다")
    void reviewThreshold_masterRequires100Reviews() {
        assertThat(ManiaLevel.REVIEW_THRESHOLD.get(ManiaLevel.MASTER)).isEqualTo(100);
    }
}
