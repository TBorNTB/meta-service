package com.sejong.metaservice.infrastructure.comment.repository;

import com.sejong.metaservice.core.comment.domain.Comment;
import com.sejong.metaservice.core.comment.repository.CommentRepository;
import com.sejong.metaservice.core.common.pagination.Cursor;
import com.sejong.metaservice.core.common.pagination.CursorPageRequest;
import com.sejong.metaservice.core.common.pagination.enums.SortDirection;
import com.sejong.metaservice.core.enums.PostType;
import com.sejong.metaservice.infrastructure.comment.entity.CommentEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

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
                cursorRequest.getSize() + 1, // next 페이지 판단용으로 1개 더 조회
                Sort.by(sortDirection, cursorRequest.getSortBy())
        );

        List<CommentEntity> commentEntities;
        if (sortDirection == Sort.Direction.ASC) {
            commentEntities = commentJpaRepository.findAllCommentsAsc(postId, postType, cursorId, pageable);
        } else {
            commentEntities = commentJpaRepository.findAllCommentsDesc(postId, postType, cursorId, pageable);
        }

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
