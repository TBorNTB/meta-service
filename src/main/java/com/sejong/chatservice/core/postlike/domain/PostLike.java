package com.sejong.chatservice.core.postlike.domain;

import com.sejong.chatservice.core.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLike {

    private Long id;
    private Long userId;
    private Long postId;
    private PostType postType;
    private LocalDateTime createdAt;

    public static PostLike from(Long userId, Long postId, PostType postType, LocalDateTime createdAt ) {
        return PostLike.builder()
                .id(null)
                .userId(userId)
                .postId(postId)
                .postType(postType)
                .createdAt(createdAt)
                .build();
    }
}
