package com.sejong.metaservice.domain.comment.command;

import com.sejong.metaservice.support.common.enums.PostType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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