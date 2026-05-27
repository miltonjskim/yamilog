package com.yamilog.common.domain.pagination;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CursorPageRequestTest {

    @Test
    @DisplayName("size 가 0 이하면 DEFAULT_SIZE 로 보정된다")
    void size_zeroOrNegative_usesDefaultSize() {
        assertThat(CursorPageRequest.of(null, 0).size()).isEqualTo(CursorPageRequest.DEFAULT_SIZE);
        assertThat(CursorPageRequest.of(null, -5).size()).isEqualTo(CursorPageRequest.DEFAULT_SIZE);
    }

    @Test
    @DisplayName("size 가 MAX_SIZE 를 초과하면 MAX_SIZE 로 보정된다")
    void size_exceedsMax_cappedAtMaxSize() {
        assertThat(CursorPageRequest.of(null, 100).size()).isEqualTo(CursorPageRequest.MAX_SIZE);
    }

    @Test
    @DisplayName("cursor 가 null 이면 첫 페이지다")
    void isFirstPage_whenCursorIsNull() {
        assertThat(CursorPageRequest.first().isFirstPage()).isTrue();
        assertThat(CursorPageRequest.of("someCursor", 20).isFirstPage()).isFalse();
    }
}
