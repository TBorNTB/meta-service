package com.sejong.metaservice.application.postlike.service;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.application.postlike.dto.response.LikeCountResponse;
import com.sejong.metaservice.application.postlike.dto.response.LikeResponse;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import com.sejong.metaservice.core.postlike.repository.LikeRepository;
import com.sejong.metaservice.infrastructure.postlike.entity.LikeStatus;
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
    public LikeResponse toggleLike(Long userId, Long postId, PostType postType) {
        postInternalFacade.checkPostExistance(postId, postType);

        PostLike like = PostLike.from(userId, postId, postType, LocalDateTime.now());
        LikeStatus toggleResult = likeRepository.toggleLike(like);

        if (toggleResult.equals(LikeStatus.LIKED)) {
            Long count = redisService.increment(RedisKeyUtil.likeCountKey(postType, postId));
            return LikeResponse.of(LikeStatus.LIKED, count);
        } else {
            Long count = redisService.decrement(RedisKeyUtil.likeCountKey(postType, postId));
            return LikeResponse.of(LikeStatus.UNLIKED, count);
        }
    }

    @Transactional(readOnly = true)
    public LikeResponse getLikeStatus(Long userId, Long postId, PostType postType) {
        String redisKey = RedisKeyUtil.likeCountKey(postType, postId);
        boolean liked = likeRepository.liked(userId, postId, postType);
        Long likeCount = redisService.getCount(redisKey);
        if (liked) return LikeResponse.of(LikeStatus.LIKED, likeCount);
        else return LikeResponse.of(LikeStatus.UNLIKED, likeCount);
    }

    @Transactional(readOnly = true)
    public LikeCountResponse getLikeCount(Long postId, PostType postType) {
        postInternalFacade.checkPostExistance(postId, postType);
        String redisKey = RedisKeyUtil.likeCountKey(postType, postId);
        long likeCount = redisService.getCount(redisKey);
        return LikeCountResponse.of(likeCount);
    }
}
