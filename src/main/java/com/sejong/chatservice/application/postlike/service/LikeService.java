package com.sejong.chatservice.application.postlike.service;

import com.sejong.chatservice.application.internal.PostInternalFacade;
import com.sejong.chatservice.application.postlike.dto.response.LikeCountResponse;
import com.sejong.chatservice.application.postlike.dto.response.LikeResponse;
import com.sejong.chatservice.application.postlike.dto.response.LikeStatusResponse;
import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.postlike.domain.PostLike;
import com.sejong.chatservice.core.postlike.repository.LikeRepository;
import com.sejong.chatservice.infrastructure.redis.RedisKeyUtil;
import com.sejong.chatservice.infrastructure.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostInternalFacade postInternalFacade;
    private final RedisService redisService;

    @Transactional
    public LikeResponse createLike(Long userId, Long postId, PostType postType) {
        postInternalFacade.checkPost(postId, postType);

        String redisKey = RedisKeyUtil.likeKey(postType, postId);

        if (redisService.isUserInLikeSet(redisKey, userId)) {
            return alreadyLikedResponse(redisKey);
        }

        redisService.addUserToLikeSet(redisKey, userId);

        likeRepository.findOne(userId, postId, postType)
                .orElseGet(() -> likeRepository.save(
                        PostLike.from(userId, postId, postType, LocalDateTime.now())
                ));

        Long likeCount = redisService.getLikeCount(redisKey);
        return LikeResponse.from(likeCount);
    }

    @Transactional
    public LikeResponse deleteLike(Long userId, Long postId, PostType postType) {
        postInternalFacade.checkPost(postId, postType);

        String redisKey = RedisKeyUtil.likeKey(postType, postId);

        redisService.removeUserFromLikeSet(redisKey, userId);

        likeRepository.findOne(userId, postId, postType)
                .ifPresent(postLike -> likeRepository.deleteById(postLike.getId()));

        Long likeCount = redisService.getLikeCount(redisKey);
        return LikeResponse.deleteFrom(likeCount);
    }

    @Transactional(readOnly = true)
    public LikeStatusResponse getLikeStatus(Long userId, Long postId, PostType postType) {
        String redisKey = RedisKeyUtil.likeKey(postType, postId);
        boolean isLiked = redisService.isUserInLikeSet(redisKey, userId);
        return LikeStatusResponse.of(isLiked);
    }

    @Transactional(readOnly = true)
    public LikeCountResponse getLikeCount(Long postId, PostType postType) {
        postInternalFacade.checkPost(postId, postType);
        String redisKey = RedisKeyUtil.likeKey(postType, postId);
        long likeCount = redisService.getLikeCount(redisKey);
        return LikeCountResponse.of(likeCount);
    }

    private LikeResponse alreadyLikedResponse(String redisKey) {
        Long likeCount = redisService.getLikeCount(redisKey);
        return LikeResponse.from(likeCount);
    }
}
