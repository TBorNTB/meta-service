package com.sejong.chatservice.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 타입 (PROJECT, ARCHIVE 등)")
public enum PostType {
    PROJECT,
    ARCHIVE,
    ;
}
