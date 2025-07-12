package com.sejong.chatservice.core.postlike;

import com.sejong.chatservice.core.enums.PostType;
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
