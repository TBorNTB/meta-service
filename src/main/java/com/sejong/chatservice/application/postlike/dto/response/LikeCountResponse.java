package com.sejong.chatservice.application.postlike.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeCountResponse {
    private Long likedCount;

    public static LikeCountResponse of(Long likedCount) {
        return LikeCountResponse.builder()
                .likedCount(likedCount)
                .build();
    }
}
