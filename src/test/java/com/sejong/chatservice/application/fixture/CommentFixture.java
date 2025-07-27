package com.sejong.chatservice.application.fixture;

import com.sejong.chatservice.core.comment.command.CommentCommand;
import com.sejong.chatservice.core.comment.command.ShowCursorCommentCommand;
import com.sejong.chatservice.core.comment.domain.Comment;
import com.sejong.chatservice.core.common.pagination.CursorPageResponse;
import com.sejong.chatservice.core.enums.PostType;

import java.time.LocalDateTime;
import java.util.List;

public class CommentFixture {

    public static Comment getComment(Long id) {
        return Comment.builder()
                .id(id)
                .content("테스트댓글입니다.")
                .userId(1L)
                .postId(1L)
                .postType(PostType.PROJECT)
                .createdAt(LocalDateTime.of(1,1,1,1,1,1))
                .updatedAt(LocalDateTime.of(1,1,1,1,1,1))
                .replyCount(2L)
                .build();
    }



    public static CursorPageResponse<Comment> getPageResponse() {
        Comment comment1 = getComment(1L);
        Comment comment2 = getComment(2L);

        List<Comment> comments = List.of(comment1, comment2);
        return CursorPageResponse.from(comments, 5);
    }

    public static CommentCommand getCommentCommand() {
        return CommentCommand.of("1",1L,PostType.PROJECT,"테스트댓글입니다.");
    }

    public static ShowCursorCommentCommand getShowCursorCommentCommand() {
        return ShowCursorCommentCommand.of(1L,PostType.PROJECT,5,LocalDateTime.now());
    }
}
