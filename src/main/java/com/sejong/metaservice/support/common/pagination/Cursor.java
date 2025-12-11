package com.sejong.metaservice.support.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cursor {
    private Long commentId;

    public static Cursor of(Long commentId) {
        return Cursor.builder()
                .commentId(commentId)
                .build();
    }
}
