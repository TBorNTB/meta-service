package com.sejong.metaservice.domain.like.dto.response;

import com.sejong.metaservice.domain.like.domain.LikeStatus;
import lombok.Builder;

@Builder
public record LikeRes(Long likeCount, LikeStatus status) {
    public static LikeRes of(LikeStatus status, Long likeCount) {
        return LikeRes
                .builder()
                .likeCount(likeCount)
                .status(status)
                .build();
    }
}
