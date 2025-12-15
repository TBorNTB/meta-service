package com.sejong.metaservice.domain.comment.service;

import static com.sejong.metaservice.domain.comment.command.CommentCommand.toComment;
import static com.sejong.metaservice.support.common.exception.ExceptionType.NOT_FOUND_COMMENT;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.domain.comment.command.CommentCommand;
import com.sejong.metaservice.domain.comment.domain.Comment;
import com.sejong.metaservice.domain.comment.dto.request.CommentReq;
import com.sejong.metaservice.domain.comment.dto.response.CommentRes;
import com.sejong.metaservice.domain.comment.repository.CommentRepository;
import com.sejong.metaservice.infrastructure.kafka.EventPublisher;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.exception.BaseException;
import com.sejong.metaservice.support.common.pagination.CursorPageRequest;
import com.sejong.metaservice.support.common.pagination.enums.SortDirection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostInternalFacade postInternalFacade;
    private final EventPublisher eventPublisher;

    @Transactional
    public CommentRes createComment(CommentCommand command) {
        String ownerUsername = postInternalFacade.checkPostExistenceAndOwner(command.getPostId(),
                command.getPostType());
        Comment comment = toComment(command);

        comment = commentRepository.save(comment);
        eventPublisher.publishCommentAlarm(comment, ownerUsername);
        return CommentRes.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentRes> getComments(CursorPageRequest cursorPageRequest, Long postId,
                                                    PostType postType) {
        Long cursorId = cursorPageRequest.getCursorId();

        Sort.Direction sortDirection = cursorPageRequest.getDirection() == SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                0,
                cursorPageRequest.getSize() + 1, // next 페이지 판단용으로 1개 더 조회
                Sort.by(sortDirection, cursorPageRequest.getSortBy())
        );

        List<Comment> comments;
        if (sortDirection == Sort.Direction.ASC) {
            comments = commentRepository.findAllCommentsAsc(postId, postType, cursorId, pageable);
        } else {
            comments = commentRepository.findAllCommentsDesc(postId, postType, cursorId, pageable);
        }

        return comments.stream().map(CommentRes::from).toList();
    }

    @Transactional
    public CommentRes updateComment(String username, Long commentId, CommentReq request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT));
        comment.validateWriter(username);
        comment.update(request.getContent(), LocalDateTime.now());
        return CommentRes.from(comment);
    }

    @Transactional
    public void deleteComment(String username, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT));
        comment.validateWriter(username);
        commentRepository.deleteById(commentId);
    }
}
