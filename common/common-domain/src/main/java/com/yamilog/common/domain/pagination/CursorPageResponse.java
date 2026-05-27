package com.yamilog.common.domain.pagination;

import java.util.List;

public record CursorPageResponse<T>(
    List<T> items,
    String nextCursor,
    boolean hasNext
) {
    public static <T> CursorPageResponse<T> of(List<T> items, String nextCursor) {
        return new CursorPageResponse<>(items, nextCursor, nextCursor != null);
    }

    public static <T> CursorPageResponse<T> empty() {
        return new CursorPageResponse<>(List.of(), null, false);
    }
}
