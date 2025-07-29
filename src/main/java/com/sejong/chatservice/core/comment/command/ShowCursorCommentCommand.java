package com.sejong.chatservice.core.comment.command;

import com.sejong.chatservice.core.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowCursorCommentCommand {

    private  Long postId;
    private  PostType postType;
    private  int size;
    private  LocalDateTime cursor;

    public static ShowCursorCommentCommand of(Long postId, PostType postType, int size, LocalDateTime cursor) {
        return new ShowCursorCommentCommand(postId, postType, size, cursor);
    }

}