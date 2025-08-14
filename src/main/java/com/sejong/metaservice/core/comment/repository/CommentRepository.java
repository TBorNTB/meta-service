package com.sejong.metaservice.core.comment.repository;

import com.sejong.metaservice.core.comment.domain.Comment;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.common.pagination.CursorPageRequest;
import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findAllComments(Long postId, PostType postType, CursorPageRequest cursorPageRequest);

    Comment updateComment(Comment comment);

    Comment findByCommentId(Long commentId);

    Long deleteComment(Long commentId);
}
