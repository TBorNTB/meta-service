package com.sejong.chatservice.application.postlike.dto.response;

import com.sejong.chatservice.core.postlike.domain.PostLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponse {
    private Long likedCount;
    private Boolean isLiked;

    public static LikeResponse from( Long likedCount) {
        return LikeResponse.builder()
                .likedCount(likedCount)
                .isLiked(Boolean.TRUE)
                .build();
    }

    public static LikeResponse deleteFrom(Long likedCount) {
        return LikeResponse.builder()
                .likedCount(likedCount)
                .isLiked(Boolean.FALSE)
                .build();
    }
}
