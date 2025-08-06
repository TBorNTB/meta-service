package com.sejong.metaservice.application.postlike.service;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.application.postlike.dto.response.LikeCountResponse;
import com.sejong.metaservice.application.postlike.dto.response.LikeResponse;
import com.sejong.metaservice.application.postlike.dto.response.LikeStatusResponse;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import com.sejong.metaservice.core.postlike.repository.LikeRepository;
import com.sejong.metaservice.infrastructure.redis.RedisKeyUtil;
import com.sejong.metaservice.infrastructure.redis.RedisService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostInternalFacade postInternalFacade;
    private final RedisService redisService;

    @Transactional
    public void toggleLike(Long userId, Long postId, PostType postType) {
        postInternalFacade.checkPostExistance(postId, postType);

        PostLike like = PostLike.from(userId, postId, postType, LocalDateTime.now());
        likeRepository.toggleLike(like);
    }

    @Transactional
    public LikeResponse createLike(Long userId, Long postId, PostType postType) {
        postInternalFacade.checkPostExistance(postId, postType);

        String redisKey = RedisKeyUtil.likeCountKey(postType, postId);

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
        postInternalFacade.checkPostExistance(postId, postType);

        String redisKey = RedisKeyUtil.likeCountKey(postType, postId);

        redisService.removeUserFromLikeSet(redisKey, userId);

        likeRepository.findOne(userId, postId, postType)
                .ifPresent(postLike -> likeRepository.deleteById(postLike.getId()));

        Long likeCount = redisService.getLikeCount(redisKey);
        return LikeResponse.deleteFrom(likeCount);
    }

    @Transactional(readOnly = true)
    public LikeStatusResponse getLikeStatus(Long userId, Long postId, PostType postType) {
        String redisKey = RedisKeyUtil.likeCountKey(postType, postId);
        boolean isLiked = redisService.isUserInLikeSet(redisKey, userId);
        return LikeStatusResponse.of(isLiked);
    }

    @Transactional(readOnly = true)
    public LikeCountResponse getLikeCount(Long postId, PostType postType) {
        postInternalFacade.checkPostExistance(postId, postType);
        String redisKey = RedisKeyUtil.likeCountKey(postType, postId);
        long likeCount = redisService.getLikeCount(redisKey);
        return LikeCountResponse.of(likeCount);
    }

    private LikeResponse alreadyLikedResponse(String redisKey) {
        Long likeCount = redisService.getLikeCount(redisKey);
        return LikeResponse.from(likeCount);
    }
}
