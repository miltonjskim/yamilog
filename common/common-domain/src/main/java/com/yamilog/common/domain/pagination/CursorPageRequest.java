package com.yamilog.common.domain.pagination;

public record CursorPageRequest(
    String cursor,
    int size
) {
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 50;

    public CursorPageRequest {
        if (size <= 0) size = DEFAULT_SIZE;
        if (size > MAX_SIZE) size = MAX_SIZE;
    }

    public static CursorPageRequest of(String cursor, int size) {
        return new CursorPageRequest(cursor, size);
    }

    public static CursorPageRequest first() {
        return new CursorPageRequest(null, DEFAULT_SIZE);
    }

    public static CursorPageRequest first(int size) {
        return new CursorPageRequest(null, size);
    }

    public boolean isFirstPage() {
        return cursor == null;
    }
}
