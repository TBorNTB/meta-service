package com.sejong.metaservice.application.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaPostCountDto {
    private Long projectCount;
    private Long csCount;
    private Long articleCount;

    public static MetaPostCountDto of(Long projectCount, Long csCount, Long articleCount) {
        return new MetaPostCountDto(projectCount, csCount, articleCount);
    }
}
