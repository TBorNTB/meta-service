package com.sejong.metaservice.application.comment.service;

import com.sejong.metaservice.application.comment.dto.request.CommentRequest;
import com.sejong.metaservice.application.comment.dto.response.CommentResponse;
import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.core.comment.command.CommentCommand;
import com.sejong.metaservice.core.comment.domain.Comment;
import com.sejong.metaservice.core.comment.repository.CommentRepository;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.common.pagination.Cursor;
import com.sejong.metaservice.core.common.pagination.CursorPageRequest;
import com.sejong.metaservice.core.common.pagination.CursorPageResponse;
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
    private final PostInternalFacade postInternalFacade;


    @Transactional
    public CommentResponse createComment(CommentCommand command) {
        postInternalFacade.checkPostExistance(command.getPostId(), command.getPostType());

        Comment comment = Comment.of(command, LocalDateTime.now());
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public CursorPageResponse<List<Comment>> getComments(CursorPageRequest cursorPageRequest, Long postId, PostType postType) {

        postInternalFacade.checkPostExistance(postId, postType);

        List<Comment> comments = commentRepository.findAllComments(
                postId,
                postType,
                cursorPageRequest);

        return CursorPageResponse.from(comments, cursorPageRequest.getSize(), comment -> Cursor.of(comment.getId()));
    }

    @Transactional
    public CommentResponse updateComment(String userId, Long commentId, CommentRequest request) {

        Comment comment = commentRepository.findByCommentId(commentId);
        comment.validateUserId(Long.valueOf(userId));
        Comment updatedComment = comment.updateComment(request.getContent(), LocalDateTime.now());
        Comment commentResponse = commentRepository.updateComment(updatedComment);
        return CommentResponse.updateFrom(commentResponse);
    }

    @Transactional
    public CommentResponse deleteComment(String userId, Long commentId) {

        Comment comment = commentRepository.findByCommentId(commentId);
        comment.validateUserId(Long.valueOf(userId));

        Long deletedId = commentRepository.deleteComment(commentId);
        return CommentResponse.deleteFrom(deletedId);
    }
}
