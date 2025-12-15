package com.sejong.metaservice.application.reply.controller;

import com.sejong.metaservice.application.reply.dto.request.ReplyCommentRequest;
import com.sejong.metaservice.application.reply.dto.response.ReplyCommentResponse;
import com.sejong.metaservice.application.reply.service.ReplyService;
import com.sejong.metaservice.core.reply.command.ReplyCreateCommand;
import com.sejong.metaservice.core.reply.domain.Reply;
import com.sejong.metaservice.support.common.pagination.CursorPageRequest;
import com.sejong.metaservice.support.common.pagination.CursorPageRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "대댓글 API", description = "댓글에 대한 대댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {
    private final ReplyService replyService;

    @Operation(summary = "대댓글 작성", description = "특정 댓글에 대댓글을 작성합니다")
    @PostMapping("/{commentParentId}")
    public ResponseEntity<ReplyCommentResponse> createReplyComment(
            @RequestHeader("X-User-Id") String username,
            @PathVariable(name="commentParentId") Long commentParentId,
            @Valid @RequestBody ReplyCommentRequest replyCommentRequest
    ){
        ReplyCreateCommand command = ReplyCreateCommand.of(replyCommentRequest.getComment(),username, commentParentId);
        ReplyCommentResponse response = replyService.createReplyComment(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "대댓글 목록 조회", description = "특정 댓글의 대댓글 목록을 커서 기반 페이징으로 조회합니다")
    @GetMapping("/{commentParentId}")
    public ResponseEntity<CursorPageRes<List<Reply>>> showReplyComments(
            @PathVariable(name="commentParentId") Long commentParentId,
            @ParameterObject @Valid CursorPageRequest cursorPageRequest
            ){
        CursorPageRes<List<Reply>> response = replyService.getAllReplyComments(commentParentId, cursorPageRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "대댓글 수정", description = "작성한 대댓글의 내용을 수정합니다")
    @PatchMapping("/{replyId}")
    public ResponseEntity<ReplyCommentResponse> updateReplyComment(
            @RequestHeader("X-User-Id") String username,
            @PathVariable(name="replyId") Long replyId,
            @Valid @RequestBody ReplyCommentRequest replyCommentRequest
    ){
        ReplyCommentResponse response = replyService.updateReplyComment(username, replyId, replyCommentRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "대댓글 삭제", description = "작성한 대댓글을 삭제합니다")
    @DeleteMapping("/{replyId}")
    public ResponseEntity<ReplyCommentResponse> deleteReplyComment(
            @RequestHeader("X-User-Id") String username,
            @PathVariable(name="replyId") Long replyId
    ){
        ReplyCommentResponse response = replyService.deleteReplyComment(username, replyId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
