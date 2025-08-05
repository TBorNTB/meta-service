package com.sejong.metaservice.application.reply.controller;

import com.sejong.metaservice.application.common.pagination.CursorPageReqDto;
import com.sejong.metaservice.application.reply.dto.request.ReplyCommentRequest;
import com.sejong.metaservice.application.reply.dto.response.ReplyCommentResponse;
import com.sejong.metaservice.application.reply.service.ReplyService;
import com.sejong.metaservice.core.common.pagination.CursorPageRequest;
import com.sejong.metaservice.core.common.pagination.CursorPageResponse;
import com.sejong.metaservice.core.reply.command.ReplyCreateCommand;
import com.sejong.metaservice.core.reply.domain.Reply;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/{commentParentId}")
    public ResponseEntity<ReplyCommentResponse> createReplyComment(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="commentParentId") Long commentParentId,
            @Valid @RequestBody ReplyCommentRequest replyCommentRequest
    ){
        ReplyCreateCommand command = ReplyCreateCommand.of(replyCommentRequest.getComment(), Long.valueOf(userId), commentParentId);
        ReplyCommentResponse response = replyService.createReplyComment(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{commentParentId}")
    public ResponseEntity<CursorPageResponse<List<Reply>>> showReplyComments(
            @PathVariable(name="commentParentId") Long commentParentId,
            @ParameterObject @Valid CursorPageReqDto cursorPageReqDto
            ){
        CursorPageRequest pageRequest = cursorPageReqDto.toPageRequest();
        CursorPageResponse<List<Reply>> response = replyService.getAllReplyComments(commentParentId, pageRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<ReplyCommentResponse> updateReplyComment(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="replyId") Long replyId,
            @Valid @RequestBody ReplyCommentRequest replyCommentRequest
    ){
        ReplyCommentResponse response = replyService.updateReplyComment(userId, replyId, replyCommentRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<ReplyCommentResponse> deleteReplyComment(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="replyId") Long replyId
    ){
        ReplyCommentResponse response = replyService.deleteReplyComment(userId, replyId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
