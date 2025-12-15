package com.sejong.metaservice.domain.comment.dto.response;

import com.sejong.metaservice.domain.comment.domain.Comment;
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
public class CommentRes {

    private Long id;
    private String content;
    private String username;
    private Long postId;
    private PostType postType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long replyCount;

    public static CommentRes from(Comment comment) {
        return CommentRes.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUsername())
                .postId(comment.getPostId())
                .replyCount(0L)
                .postType(comment.getPostType())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
