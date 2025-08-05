package com.sejong.metaservice.application.view.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewCountResponse {
    private Long viewCount;
    
    public static ViewCountResponse of(Long viewCount) {
        return ViewCountResponse.builder()
                .viewCount(viewCount)
                .build();
    }
}