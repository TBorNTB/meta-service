package com.sejong.chatservice.core.common.pagination;

import com.sejong.chatservice.core.common.pagination.enums.SortDirection;
import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class CursorPageRequest {
    Cursor cursor;
    int size;
    String sortBy;
    SortDirection direction;

    private CursorPageRequest(Cursor cursor, int size, String sortBy, SortDirection direction) {
        this.cursor = cursor;
        this.size = size;
        this.sortBy = sortBy;
        this.direction = direction;
    }

    public static CursorPageRequest of(Cursor cursor, int size, String sortBy, SortDirection direction) {
        return new CursorPageRequest(cursor, size, sortBy, direction);
    }
}
