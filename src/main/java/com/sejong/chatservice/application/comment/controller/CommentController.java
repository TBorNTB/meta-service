package com.sejong.chatservice.application.comment.controller;

import com.sejong.chatservice.application.pagination.CursorPageReqDto;
import com.sejong.chatservice.core.comment.command.CommentCommand;
import com.sejong.chatservice.application.comment.dto.request.CommentRequest;
import com.sejong.chatservice.application.comment.dto.response.CommentResponse;
import com.sejong.chatservice.application.comment.service.CommentService;
import com.sejong.chatservice.core.comment.domain.Comment;
import com.sejong.chatservice.core.common.pagination.CursorPageRequest;
import com.sejong.chatservice.core.common.pagination.CursorPageResponse;
import com.sejong.chatservice.core.enums.PostType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponse> createComment(
            @RequestHeader("x-user") String userId,
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "postType") PostType postType,
            @Valid @RequestBody CommentRequest request
    ) {
        CommentCommand command = CommentCommand.of(userId, postId, postType, request.getContent());
        CommentResponse response = commentService.createComment(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CursorPageResponse<List<Comment>>> showComments(
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "postType") PostType postType,
            @ParameterObject @Valid CursorPageReqDto cursorPageReqDto
    ) {
        CursorPageRequest cursorRequest = cursorPageReqDto.toPageRequest();
        CursorPageResponse<List<Comment>> comments = commentService.getComments(cursorRequest, postId, postType);
        return ResponseEntity.ok(comments);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @RequestHeader("x-user") String userId,
            @PathVariable(name = "commentId") Long commentId,
            @Valid @RequestBody CommentRequest request
    ) {

        CommentResponse response = commentService.updateComment(userId, commentId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(
            @RequestHeader("x-user") String userId,
            @PathVariable(name = "commentId") Long commentId
    ) {
        CommentResponse response = commentService.deleteComment(userId, commentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
