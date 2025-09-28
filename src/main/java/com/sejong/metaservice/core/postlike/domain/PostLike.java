package com.sejong.metaservice.core.postlike.domain;

import com.sejong.metaservice.core.common.enums.PostType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLike {

    private Long id;
    private String username;
    private Long postId;
    private PostType postType;
    private LocalDateTime createdAt;

    public static PostLike from(String username, Long postId, PostType postType, LocalDateTime createdAt ) {
        return PostLike.builder()
                .id(null)
                .username(username)
                .postId(postId)
                .postType(postType)
                .createdAt(createdAt)
                .build();
    }
}
