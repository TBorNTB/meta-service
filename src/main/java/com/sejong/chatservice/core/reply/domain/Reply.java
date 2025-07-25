package com.sejong.chatservice.core.reply.domain;

import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import com.sejong.chatservice.core.reply.command.ReplyCreateCommand;
import com.sejong.chatservice.core.common.HasCreatedAt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reply implements HasCreatedAt {

    private Long id;
    private String content;
    private Long userId;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Reply of(ReplyCreateCommand command, LocalDateTime now) {
        return Reply.builder()
                .id(null)
                .content(command.getContent())
                .userId(command.getUserId())
                .parentCommentId(command.getCommentParentId())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void validateUserId(Long userId) {
        if(!this.userId.equals(userId)) {
            throw new ApiException(ErrorCode.BAD_REQUEST,"유저 정보 틀려요");
        }
    }

    public Reply updateReply( String comment, LocalDateTime now) {
        this.content = comment;
        this.updatedAt = now;
        return this;
    }
}
