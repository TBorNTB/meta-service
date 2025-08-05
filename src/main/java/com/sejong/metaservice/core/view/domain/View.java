package com.sejong.metaservice.core.view.domain;

import com.sejong.metaservice.core.common.enums.PostType;
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

    public static View of(PostType postType, Long postId, Long viewCount) {
        return View.builder()
                .postType(postType)
                .postId(postId)
                .viewCount(viewCount)
                .build();
    }
}
