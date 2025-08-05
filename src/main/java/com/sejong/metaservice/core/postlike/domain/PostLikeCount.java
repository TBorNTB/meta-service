package com.sejong.metaservice.core.postlike.domain;

import com.sejong.metaservice.core.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLikeCount {

    private Long id;
    private Long postId;
    private PostType postType;
    private Long count;
}
