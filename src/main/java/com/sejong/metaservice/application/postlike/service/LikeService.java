package com.sejong.metaservice.application.postlike.service;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.application.postlike.dto.response.LikeCountResponse;
import com.sejong.metaservice.application.postlike.dto.response.LikeResponse;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import com.sejong.metaservice.core.postlike.repository.LikeRepository;
import com.sejong.metaservice.infrastructure.kafka.EventPublisher;
import com.sejong.metaservice.infrastructure.postlike.entity.LikeStatus;
import com.sejong.metaservice.infrastructure.redis.RedisKeyUtil;
import com.sejong.metaservice.infrastructure.redis.RedisService;
import com.sejong.metaservice.support.common.enums.PostType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostInternalFacade postInternalFacade;
    private final RedisService redisService;
    private final EventPublisher postlikeEventPublisher;

    @Transactional
    public LikeResponse toggleLike(String username, Long postId, PostType postType) {
        String ownerUsername = postInternalFacade.checkPostExistanceAndOwner(postId, postType);
        log.info("유저이름 : {}",ownerUsername);

        PostLike like = PostLike.from(username, postId, postType, LocalDateTime.now());
        LikeStatus toggleResult = likeRepository.toggleLike(like);

        if (toggleResult.equals(LikeStatus.LIKED)) {
            Long count = redisService.increment(RedisKeyUtil.likeCountKey(postType, postId));
            postlikeEventPublisher.publishLike(like,count);
            postlikeEventPublisher.publishLikedAlarm(like,ownerUsername);
            return LikeResponse.of(LikeStatus.LIKED, count);
        } else {
            Long count = redisService.decrement(RedisKeyUtil.likeCountKey(postType, postId));
            postlikeEventPublisher.publishLike(like,count);
            return LikeResponse.of(LikeStatus.UNLIKED, count);
        }
    }

    @Transactional(readOnly = true)
    public LikeResponse getLikeStatus(String username, Long postId, PostType postType) {
        String redisKey = RedisKeyUtil.likeCountKey(postType, postId);
        boolean liked = likeRepository.liked(username, postId, postType);
        Long likeCount = redisService.getCount(redisKey);
        if (liked) return LikeResponse.of(LikeStatus.LIKED, likeCount);
        else return LikeResponse.of(LikeStatus.UNLIKED, likeCount);
    }

    @Transactional(readOnly = true)
    public LikeCountResponse getLikeCount(Long postId, PostType postType) {
        postInternalFacade.checkPostExistanceAndOwner(postId, postType);
        String redisKey = RedisKeyUtil.likeCountKey(postType, postId);
        long likeCount = redisService.getCount(redisKey);
        return LikeCountResponse.of(likeCount);
    }
}
