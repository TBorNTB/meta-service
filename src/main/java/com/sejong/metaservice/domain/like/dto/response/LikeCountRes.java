package com.sejong.metaservice.domain.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeCountRes {
    private Long likedCount;

    public static LikeCountRes of(Long likedCount) {
        return LikeCountRes.builder()
                .likedCount(likedCount)
                .build();
    }
}
