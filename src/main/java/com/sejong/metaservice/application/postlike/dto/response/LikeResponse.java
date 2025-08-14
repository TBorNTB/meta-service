package com.sejong.metaservice.application.postlike.dto.response;

import com.sejong.metaservice.infrastructure.postlike.entity.LikeStatus;
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
    private LikeStatus result;

    public static LikeResponse of(LikeStatus result, Long likedCount) {
        return LikeResponse.builder()
                .likedCount(likedCount)
                .result(result)
                .build();
    }
}
