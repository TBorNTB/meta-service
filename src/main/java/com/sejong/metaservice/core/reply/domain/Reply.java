package com.sejong.metaservice.core.reply.domain;

import static com.sejong.metaservice.core.common.exception.ExceptionType.BAD_REQUEST;

import com.sejong.metaservice.core.common.exception.BaseException;
import com.sejong.metaservice.core.reply.command.ReplyCreateCommand;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reply {

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
            throw new BaseException(BAD_REQUEST);
        }
    }

    public Reply updateReply( String comment, LocalDateTime now) {
        this.content = comment;
        this.updatedAt = now;
        return this;
    }
}
