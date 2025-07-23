package com.sejong.chatservice.application.reply.service;

import com.sejong.chatservice.application.reply.command.ReplyCreateCommand;
import com.sejong.chatservice.application.reply.dto.request.ReplyCommentRequest;
import com.sejong.chatservice.application.reply.dto.response.ReplyCommentResponse;
import com.sejong.chatservice.core.common.PageResponse;
import com.sejong.chatservice.core.reply.domain.Reply;
import com.sejong.chatservice.core.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    @Transactional
    public ReplyCommentResponse createReplyComment(ReplyCreateCommand command) {
        //todo userId 관련 검증해야됩니다~
        Reply reply = Reply.of(command, LocalDateTime.now());
        Reply responseReply = replyRepository.save(reply);
        return ReplyCommentResponse.from(responseReply);
    }

    @Transactional(readOnly = true)
    public PageResponse<Reply> getAllReplyComments(Long commentParentId, int size, LocalDateTime cursor) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Reply> replies = replyRepository.findAllReplyComments(commentParentId, cursor, pageable);
        return PageResponse.from(replies,size);
    }

    @Transactional
    public ReplyCommentResponse updateReplyComment(String userId, Long replyId, ReplyCommentRequest request) {
       Reply reply = replyRepository.findById(replyId);
       reply.validateUserId(Long.valueOf(userId));

        Reply updatedReply = reply.updateReply(request.getComment(), LocalDateTime.now());
        Reply responseReply = replyRepository.updateReply(updatedReply);
        return ReplyCommentResponse.from(responseReply);
    }

    @Transactional
    public ReplyCommentResponse deleteReplyComment(String userId, Long replyId) {
        Reply reply = replyRepository.findById(replyId);
        reply.validateUserId(Long.valueOf(userId));

        Long deletedId = replyRepository.deleteById(replyId);
        return ReplyCommentResponse.deleteFrom(deletedId);
    }
}
