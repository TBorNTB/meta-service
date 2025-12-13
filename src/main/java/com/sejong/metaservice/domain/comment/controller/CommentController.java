package com.sejong.metaservice.domain.comment.controller;

import com.sejong.metaservice.domain.comment.command.CommentCommand;
import com.sejong.metaservice.domain.comment.domain.Comment;
import com.sejong.metaservice.domain.comment.dto.request.CommentRequest;
import com.sejong.metaservice.domain.comment.dto.response.CommentResponse;
import com.sejong.metaservice.domain.comment.service.CommentService;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.pagination.CursorPageRequest;
import com.sejong.metaservice.support.common.pagination.CursorPageResponse;
import com.sejong.metaservice.support.pagination.CursorPageReqDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "댓글 API", description = "게시물 댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "특정 게시물에 댓글을 작성합니다")
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponse> createComment(
            @RequestHeader("X-User-Id") String username,
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "postType") PostType postType,
            @Valid @RequestBody CommentRequest request
    ) {
        CommentCommand command = CommentCommand.of(username, postId, postType, request.getContent());
        CommentResponse response = commentService.createComment(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "댓글 목록 조회", description = "특정 게시물의 댓글 목록을 커서 기반 페이징으로 조회합니다")
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

    @Operation(summary = "댓글 수정", description = "작성한 댓글의 내용을 수정합니다")
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @RequestHeader("X-User-Id") String username,
            @PathVariable(name = "commentId") Long commentId,
            @Valid @RequestBody CommentRequest request
    ) {

        CommentResponse response = commentService.updateComment(username, commentId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(
            @RequestHeader("X-User-Id") String username,
            @PathVariable(name = "commentId") Long commentId
    ) {
        CommentResponse response = commentService.deleteComment(username, commentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
