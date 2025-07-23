package com.sejong.chatservice.application.reply.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyCreateCommand {
    private String content;
    private Long userId;
    private Long commentParentId;

    public static ReplyCreateCommand of(String content, Long userId, Long commentParentId) {
        return new ReplyCreateCommand(content, userId, commentParentId);
    }

}
