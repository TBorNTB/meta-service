package com.sejong.chatservice.infrastructure.comment.repository;

import com.sejong.chatservice.core.comment.domain.Comment;
import com.sejong.chatservice.core.comment.repository.CommentRepository;
import com.sejong.chatservice.core.common.PageSearchCommand;
import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.infrastructure.comment.entity.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<Comment> findAllComments(Long postId, PostType postType, PageSearchCommand command) {
        Pageable pageable = PageRequest.of(
                0,
                command.getSize(),
                Sort.by(Sort.Direction.fromString(command.getDirection()), command.getSort())
        );
        List<CommentEntity> commentEntities = commentJpaRepository.findAllComments(postId, postType, command.getCursor(), pageable);

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
