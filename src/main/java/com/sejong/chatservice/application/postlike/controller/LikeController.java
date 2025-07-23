package com.sejong.chatservice.application.postlike.controller;

import com.sejong.chatservice.application.postlike.dto.response.LikeCountResponse;
import com.sejong.chatservice.application.postlike.dto.response.LikeResponse;
import com.sejong.chatservice.application.postlike.dto.response.LikeStatusResponse;
import com.sejong.chatservice.application.postlike.service.LikeService;
import com.sejong.chatservice.core.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<LikeResponse> createLike(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeResponse response = likeService.createLike(userId, postId, postType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<LikeResponse> deleteLike(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeResponse response = likeService.deleteLike(userId, postId, postType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{postId}/me")
    public ResponseEntity<LikeStatusResponse> getLike(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeStatusResponse response = likeService.getLikeStatus(userId, postId, postType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{postId}/count")
    public ResponseEntity<LikeCountResponse> getLikeCount(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeCountResponse response = likeService.getLikeCount(userId, postId, postType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
