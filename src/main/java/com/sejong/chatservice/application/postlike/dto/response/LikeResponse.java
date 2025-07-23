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
    private Long likedId;
    private String message;
    private Long likedCount;

    public static LikeResponse from(PostLike postLike, Long likedCount) {
        return LikeResponse.builder()
                .likedId(postLike.getId())
                .likedCount(likedCount)
                .message("좋아요가 성공적으로 저장되었습니다.")
                .build();
    }

    public static LikeResponse deleteFrom(Long deletedId, Long likedCount) {
        return LikeResponse.builder()
                .likedId(deletedId)
                .likedCount(likedCount)
                .message("좋아요가 취소되었습니다.")
                .build();
    }
}
