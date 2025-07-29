package com.sejong.chatservice.core.common.pagination;

import com.sejong.chatservice.core.enums.PostType;
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
