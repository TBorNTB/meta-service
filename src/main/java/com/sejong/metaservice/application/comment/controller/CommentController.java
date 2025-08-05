package com.sejong.metaservice.application.comment.controller;

import com.sejong.metaservice.application.comment.dto.request.CommentRequest;
import com.sejong.metaservice.application.comment.dto.response.CommentResponse;
import com.sejong.metaservice.application.comment.service.CommentService;
import com.sejong.metaservice.application.common.pagination.CursorPageReqDto;
import com.sejong.metaservice.core.comment.command.CommentCommand;
import com.sejong.metaservice.core.comment.domain.Comment;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.common.pagination.CursorPageRequest;
import com.sejong.metaservice.core.common.pagination.CursorPageResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
