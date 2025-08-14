package com.sejong.metaservice.core.comment.domain;

import com.sejong.metaservice.core.comment.command.CommentCommand;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.common.exception.BaseException;
import com.sejong.metaservice.core.common.exception.ExceptionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private PostType postType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long replyCount;

    public static Comment of(CommentCommand command, LocalDateTime now) {
        return Comment.builder()
                .id(null)
                .content(command.getContent())
                .userId(command.getUserId())
                .postId(command.getPostId())
                .replyCount(0L)
                .postType(command.getPostType())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public Comment updateComment(String content, LocalDateTime updatedAt) {
        this.content = content;
        this.updatedAt = updatedAt;
        return this;
    }

    public void validateUserId(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new BaseException(ExceptionType.BAD_REQUEST);
        }
    }
}
