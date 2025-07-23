package com.sejong.chatservice.core.comment.domain;

import com.sejong.chatservice.application.comment.command.CommentCommand;
import com.sejong.chatservice.application.error.code.ErrorCode;
import com.sejong.chatservice.application.error.exception.ApiException;
import com.sejong.chatservice.core.common.HasCreatedAt;
import com.sejong.chatservice.core.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment implements HasCreatedAt {

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
            throw new ApiException(ErrorCode.BAD_REQUEST, "Invalid user id");
        }
    }
}
