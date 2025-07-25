package com.sejong.chatservice.core.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageSearchCommand {
    private int size;
    private LocalDateTime cursor;
    private String sort;
    private String direction;

    public static PageSearchCommand of(int size, LocalDateTime cursor , String sort, String direction) {
        return PageSearchCommand.builder()
                .size(size)
                .cursor(cursor)
                .sort(sort)
                .direction(direction)
                .build();
    }
}