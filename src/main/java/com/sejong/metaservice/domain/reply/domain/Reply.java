package com.sejong.metaservice.domain.reply.domain;

import static com.sejong.metaservice.support.common.exception.ExceptionType.BAD_REQUEST;

import com.sejong.metaservice.domain.reply.ReplyCreateCommand;
import com.sejong.metaservice.support.common.exception.BaseException;
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
    private String username;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Reply of(ReplyCreateCommand command, LocalDateTime now) {
        return Reply.builder()
                .id(null)
                .content(command.getContent())
                .username(command.getUsername())
                .parentCommentId(command.getCommentParentId())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void validateUserId(String username) {
        if(!this.username.equals(username)) {
            throw new BaseException(BAD_REQUEST);
        }
    }

    public Reply updateReply( String comment, LocalDateTime now) {
        this.content = comment;
        this.updatedAt = now;
        return this;
    }
}
