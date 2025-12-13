package com.sejong.metaservice.core.comment.domain;

import com.sejong.metaservice.core.comment.command.CommentCommand;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.exception.BaseException;
import com.sejong.metaservice.support.common.exception.ExceptionType;
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
    private String username;
    private Long postId;
    private PostType postType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long replyCount;

    public static Comment of(CommentCommand command, LocalDateTime now) {
        return Comment.builder()
                .id(null)
                .content(command.getContent())
                .username(command.getUsername())
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

    public void validateUserId(String username) {
        if (!this.username.equals(username)) {
            throw new BaseException(ExceptionType.BAD_REQUEST);
        }
    }
}
