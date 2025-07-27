package com.sejong.chatservice.core.comment.repository;

import com.sejong.chatservice.core.comment.domain.Comment;
import com.sejong.chatservice.core.common.pagination.CursorPageRequest;
import com.sejong.chatservice.core.common.pagination.PageSearchCommand;
import com.sejong.chatservice.core.enums.PostType;
import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findAllComments(Long postId, PostType postType, CursorPageRequest cursorPageRequest);

    Comment updateComment(Comment comment);

    Comment findByCommentId(Long commentId);

    Long deleteComment(Long commentId);
}
