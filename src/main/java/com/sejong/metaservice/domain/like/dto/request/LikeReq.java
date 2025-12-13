package com.sejong.metaservice.domain.like.dto.request;

import com.sejong.metaservice.domain.like.domain.Like;
import com.sejong.metaservice.support.common.enums.PostType;
import java.time.LocalDateTime;


public record LikeReq(
    Long id,
    String username,
    Long postId,
    PostType postType,
    LocalDateTime createdAt
){
    public static Like from(String username, Long postId, PostType postType, LocalDateTime createdAt) {
        return Like.builder()
                .id(null)
                .username(username)
                .postId(postId)
                .postType(postType)
                .createdAt(createdAt)
                .build();
    }
}
