package com.sejong.metaservice.domain.comment.service;

import static com.sejong.metaservice.support.common.exception.ExceptionType.NOT_FOUND_COMMENT;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.domain.comment.command.CommentCommand;
import com.sejong.metaservice.domain.comment.domain.Comment;
import com.sejong.metaservice.domain.comment.domain.CommentEntity;
import com.sejong.metaservice.domain.comment.dto.request.CommentRequest;
import com.sejong.metaservice.domain.comment.dto.response.CommentResponse;
import com.sejong.metaservice.domain.comment.repository.CommentJpaRepository;
import com.sejong.metaservice.domain.comment.repository.CommentRepository;
import com.sejong.metaservice.infrastructure.kafka.EventPublisher;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.exception.BaseException;
import com.sejong.metaservice.support.common.pagination.Cursor;
import com.sejong.metaservice.support.common.pagination.CursorPageRequest;
import com.sejong.metaservice.support.common.pagination.CursorPageResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final PostInternalFacade postInternalFacade;
    private final EventPublisher eventPublisher;

    @Transactional
    public CommentResponse createComment(CommentCommand command) {
        String ownerUsername = postInternalFacade.checkPostExistenceAndOwner(command.getPostId(), command.getPostType());
        Comment comment = Comment.of(command, LocalDateTime.now());
        CommentEntity commentEntity = CommentEntity.from(comment);
        Comment savedComment = commentJpaRepository.save(commentEntity).toDomain();
        eventPublisher.publishCommentAlarm(savedComment, ownerUsername);
        return CommentResponse.from(savedComment);
    }

    @Transactional(readOnly = true)
    public CursorPageResponse<List<Comment>> getComments(CursorPageRequest cursorPageRequest, Long postId, PostType postType) {
        List<Comment> comments = commentRepository.findAllComments(
                postId,
                postType,
                cursorPageRequest);

        return CursorPageResponse.from(comments, cursorPageRequest.getSize(), comment -> Cursor.of(comment.getId()));
    }

    @Transactional
    public CommentResponse updateComment(String username, Long commentId, CommentRequest request) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT)).toDomain();
        comment.validateUserId(username);
        Comment updatedComment = comment.updateComment(request.getContent(), LocalDateTime.now());
        Comment commentResponse = commentRepository.updateComment(updatedComment);
        return CommentResponse.updateFrom(commentResponse);
    }

    @Transactional
    public CommentResponse deleteComment(String username, Long commentId) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT)).toDomain();
        comment.validateUserId(username);

        Long deletedId = commentRepository.deleteComment(commentId);
        return CommentResponse.deleteFrom(deletedId);
    }
}
