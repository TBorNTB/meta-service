package com.sejong.metaservice.application.postlike.controller;

import com.sejong.metaservice.application.postlike.dto.response.LikeCountResponse;
import com.sejong.metaservice.application.postlike.dto.response.LikeResponse;
import com.sejong.metaservice.application.postlike.dto.response.LikeStatusResponse;
import com.sejong.metaservice.application.postlike.service.LikeService;
import com.sejong.metaservice.core.common.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<LikeResponse> createLike(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeResponse response = likeService.createLike(Long.valueOf(userId), postId, postType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<LikeResponse> deleteLike(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeResponse response = likeService.deleteLike(Long.valueOf(userId), postId, postType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{postId}/me")
    public ResponseEntity<LikeStatusResponse> getLike(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeStatusResponse response = likeService.getLikeStatus(Long.valueOf(userId), postId, postType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{postId}/count")
    public ResponseEntity<LikeCountResponse> getLikeCount(
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeCountResponse response = likeService.getLikeCount(postId, postType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
