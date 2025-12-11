package com.sejong.metaservice.core.view.domain;

import com.sejong.metaservice.support.common.enums.PostType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class View {

    private Long id;
    private PostType postType;
    private Long postId;
    private Long viewCount;
    private LocalDateTime updatedAt;

    public static View of(PostType postType, Long postId, Long viewCount) {
        return View.builder()
                .postType(postType)
                .postId(postId)
                .viewCount(viewCount)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
