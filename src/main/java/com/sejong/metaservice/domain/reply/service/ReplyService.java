package com.sejong.metaservice.domain.reply.service;

import static com.sejong.metaservice.support.common.exception.ExceptionType.NOT_FOUND_COMMENT;

import com.sejong.metaservice.domain.comment.domain.Comment;
import com.sejong.metaservice.domain.comment.repository.CommentRepository;
import com.sejong.metaservice.domain.reply.ReplyCreateCommand;
import com.sejong.metaservice.domain.reply.domain.Reply;
import com.sejong.metaservice.domain.reply.dto.request.ReplyCommentRequest;
import com.sejong.metaservice.domain.reply.dto.response.ReplyCommentResponse;
import com.sejong.metaservice.domain.reply.repository.ReplyRepository;
import com.sejong.metaservice.support.common.exception.BaseException;
import com.sejong.metaservice.support.common.kafka.EventPublisher;
import com.sejong.metaservice.support.common.pagination.CursorPageRequest;
import com.sejong.metaservice.support.common.pagination.CursorPageRes;
import java.time.LocalDateTime;
import java.util.List;
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
        Comment parentComment = commentRepository.findById(responseReply.getParentCommentId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT));
        eventPublisher.publishReplyAlarm(parentComment , responseReply);
        return ReplyCommentResponse.from(responseReply);
    }

    @Transactional(readOnly = true)
    public CursorPageRes<List<Reply>> getAllReplyComments(Long commentParentId, CursorPageRequest cursorPageRequest) {

        List<Reply> replies = replyRepository.findAllReplyComments(commentParentId, cursorPageRequest);
        return CursorPageRes.from(replies, cursorPageRequest.getSize(), Reply::getId);
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
