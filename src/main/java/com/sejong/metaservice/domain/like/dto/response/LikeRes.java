package com.sejong.metaservice.domain.like.dto.response;

import com.sejong.metaservice.domain.like.domain.LikeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeRes {
    private Long likedCount;
    private LikeStatus result;

    public static LikeRes of(LikeStatus result, Long likedCount) {
        return LikeRes.builder()
                .likedCount(likedCount)
                .result(result)
                .build();
    }
}
