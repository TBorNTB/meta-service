package com.sejong.chatservice.core.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult<T> {

    private List<T> content;
    private LocalDateTime nextCursor;
    private boolean hasNext;

    public static <T extends HasCreatedAt> PageResult<T> from(List<T> content, int size) {
        LocalDateTime nextCursor = content.isEmpty()
                ? null
                : content.get(content.size() - 1).getCreatedAt();

        boolean hasNext = content.size() == size;

        return PageResult.<T>builder()
                .content(content)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
