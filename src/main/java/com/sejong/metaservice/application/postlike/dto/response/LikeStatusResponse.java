package com.sejong.metaservice.application.postlike.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeStatusResponse {
    boolean isLiked;

    public static LikeStatusResponse of(boolean isLiked) {
        return new LikeStatusResponse(isLiked);
    }
}
