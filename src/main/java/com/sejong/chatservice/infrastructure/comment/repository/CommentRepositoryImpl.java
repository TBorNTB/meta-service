package com.sejong.chatservice.infrastructure.comment.repository;

import com.sejong.chatservice.core.comment.domain.Comment;
import com.sejong.chatservice.core.comment.repository.CommentRepository;
import com.sejong.chatservice.core.common.pagination.Cursor;
import com.sejong.chatservice.core.common.pagination.CursorPageRequest;
import com.sejong.chatservice.core.common.pagination.PageSearchCommand;
import com.sejong.chatservice.core.common.pagination.enums.SortDirection;
import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.infrastructure.comment.entity.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        CommentEntity commentEntity = CommentEntity.from(comment);
        CommentEntity responseEntity = commentJpaRepository.save(commentEntity);
        return responseEntity.toDomain();
    }

    @Override
    public List<Comment> findAllComments(Long postId, PostType postType, CursorPageRequest cursorRequest) {
        Long cursorId = Optional.ofNullable(cursorRequest.getCursor())
                .map(Cursor::getCommentId)
                .orElse(null);

        Sort.Direction sortDirection = cursorRequest.getDirection() == SortDirection.ASC
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
            
        Pageable pageable = PageRequest.of(
                0,
                cursorRequest.getSize() + 1,
                Sort.by(sortDirection, cursorRequest.getSortBy())
        );

        List<CommentEntity> commentEntities = commentJpaRepository.findAllComments(
                postId,
                postType,
                cursorId,
                pageable
        );

        return commentEntities.stream()
                .map(CommentEntity::toDomain)
                .toList();
    }

    @Override
    public Comment updateComment(Comment comment) {
        CommentEntity commentEntity = commentJpaRepository.findById(comment.getId())
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        Comment updatedComment = commentEntity.updateComment(comment.getContent(), comment.getUpdatedAt());
        return updatedComment;
    }

    @Override
    public Comment findByCommentId(Long commentId) {
        CommentEntity entity = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        return entity.toDomain();
    }

    @Override
    public Long deleteComment(Long commentId) {
        commentJpaRepository.deleteById(commentId);
        return commentId;
    }
}
