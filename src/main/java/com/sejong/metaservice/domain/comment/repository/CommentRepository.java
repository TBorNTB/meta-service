package com.sejong.metaservice.domain.comment.repository;

import com.sejong.metaservice.domain.comment.domain.Comment;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.pagination.CursorPageRequest;
import java.util.List;

public interface CommentRepository {
    List<Comment> findAllComments(Long postId, PostType postType, CursorPageRequest cursorPageRequest);

    Comment updateComment(Comment comment);

    Comment findByCommentId(Long commentId);

    Long deleteComment(Long commentId);
}
