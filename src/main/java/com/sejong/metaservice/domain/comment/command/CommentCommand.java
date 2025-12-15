package com.sejong.metaservice.domain.comment.command;

import com.sejong.metaservice.domain.comment.domain.Comment;
import com.sejong.metaservice.support.common.enums.PostType;
import java.time.LocalDateTime;
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

    public static Comment toComment(CommentCommand command) {
        return Comment.builder()
                .id(null)
                .content(command.getContent())
                .username(command.getUsername())
                .postId(command.getPostId())
                .postType(command.getPostType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
