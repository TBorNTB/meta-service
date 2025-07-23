package com.sejong.chatservice.core.comment.repository;

import com.sejong.chatservice.core.comment.domain.Comment;
import com.sejong.chatservice.core.enums.PostType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findAllComments(Long postId, PostType postType, LocalDateTime cursor, Pageable pageable);

    Comment updateComment(Comment comment);

    Comment findByCommentId(Long commentId);

    Long deleteComment(Long commentId);
}
