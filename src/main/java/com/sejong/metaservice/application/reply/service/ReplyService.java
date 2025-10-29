package com.sejong.metaservice.application.reply.service;

import com.sejong.metaservice.application.reply.dto.request.ReplyCommentRequest;
import com.sejong.metaservice.application.reply.dto.response.ReplyCommentResponse;
import com.sejong.metaservice.core.comment.domain.Comment;
import com.sejong.metaservice.core.comment.repository.CommentRepository;
import com.sejong.metaservice.core.common.pagination.Cursor;
import com.sejong.metaservice.core.common.pagination.CursorPageRequest;
import com.sejong.metaservice.core.common.pagination.CursorPageResponse;
import com.sejong.metaservice.core.reply.command.ReplyCreateCommand;
import com.sejong.metaservice.core.reply.domain.Reply;
import com.sejong.metaservice.core.reply.repository.ReplyRepository;
import java.time.LocalDateTime;
import java.util.List;

import com.sejong.metaservice.infrastructure.kafka.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final EventPublisher eventPublisher;
    private final CommentRepository commentRepository;

    @Transactional
    public ReplyCommentResponse createReplyComment(ReplyCreateCommand command) {
        Reply reply = Reply.of(command, LocalDateTime.now());
        Reply responseReply = replyRepository.save(reply);
        Comment parentComment = commentRepository.findByCommentId(responseReply.getParentCommentId());
        eventPublisher.publishReplyAlarm(parentComment , responseReply);
        return ReplyCommentResponse.from(responseReply);
    }

    @Transactional(readOnly = true)
    public CursorPageResponse<List<Reply>> getAllReplyComments(Long commentParentId, CursorPageRequest cursorPageRequest) {

        List<Reply> replies = replyRepository.findAllReplyComments(commentParentId, cursorPageRequest);
        return CursorPageResponse.from(replies,cursorPageRequest.getSize(),reply -> Cursor.of(reply.getId()));
    }


    @Transactional
    public ReplyCommentResponse updateReplyComment(String username, Long replyId, ReplyCommentRequest request) {

        Reply reply = replyRepository.findById(replyId);
        reply.validateUserId(username);

        Reply updatedReply = reply.updateReply(request.getComment(), LocalDateTime.now());
        Reply responseReply = replyRepository.updateReply(updatedReply);
        return ReplyCommentResponse.from(responseReply);
    }

    @Transactional
    public ReplyCommentResponse deleteReplyComment(String username, Long replyId) {
        Reply reply = replyRepository.findById(replyId);
        reply.validateUserId(username);

        Long deletedId = replyRepository.deleteById(replyId);
        return ReplyCommentResponse.deleteFrom(deletedId);
    }
}
