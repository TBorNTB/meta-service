package com.sejong.metaservice.application.postlike.controller;

import com.sejong.metaservice.application.postlike.dto.response.LikeCountResponse;
import com.sejong.metaservice.application.postlike.dto.response.LikeResponse;
import com.sejong.metaservice.application.postlike.service.LikeService;
import com.sejong.metaservice.core.common.enums.PostType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{postId}")
    @Operation(summary = "좋아요 토글 액션")
    public ResponseEntity<LikeResponse> toggleLike(
            @RequestHeader("X-User-Id") String username,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeResponse likeResponse = likeService.toggleLike(username, postId, postType);
        return ResponseEntity.ok(likeResponse);
    }

    @GetMapping("/{postId}/me")
    @Operation(summary = "해당 포스트에 대한 유저의 좋아요 여부 및 좋아요 수")
    public ResponseEntity<LikeResponse> getLike(
            @RequestHeader("X-User-Id") String username,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeResponse response = likeService.getLikeStatus(username, postId, postType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{postId}/count")
    @Operation(summary = "해당 글에 대한 좋아요 수")
    public ResponseEntity<LikeCountResponse> getLikeCount(
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeCountResponse response = likeService.getLikeCount(postId, postType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
