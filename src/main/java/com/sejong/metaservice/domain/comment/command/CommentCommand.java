package com.sejong.metaservice.domain.comment.command;

import com.sejong.metaservice.support.common.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCommand {
    private  String username;
    private  Long postId;
    private  PostType postType;
    private  String content;

    public static CommentCommand of(String username, Long postId, PostType postType, String content) {
        return new CommentCommand(username, postId, postType, content);
    }
}
