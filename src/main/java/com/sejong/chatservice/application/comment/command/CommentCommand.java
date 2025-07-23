package com.sejong.chatservice.application.comment.command;

import com.sejong.chatservice.core.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCommand {
    private  Long userId;
    private  Long postId;
    private  PostType postType;
    private  String content;

    public static CommentCommand of(String userId, Long postId, PostType postType, String content) {
        return new CommentCommand(Long.valueOf(userId), postId, postType, content);
    }
}
