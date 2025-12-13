package com.sejong.metaservice.domain.view.dto.response;

import lombok.Builder;

@Builder
public record ViewCountResponse (
        Long viewCount
){
}