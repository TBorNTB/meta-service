package com.sejong.metaservice.core.reply.command;

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
    private String username;
    private Long commentParentId;

    public static ReplyCreateCommand of(String content, String username, Long commentParentId) {
        return new ReplyCreateCommand(content, username, commentParentId);
    }

}
