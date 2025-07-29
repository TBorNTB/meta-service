package com.sejong.chatservice.application.postlike.service;

import com.sejong.chatservice.application.internal.PostInternalFacade;
import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import com.sejong.chatservice.application.postlike.dto.response.LikeCountResponse;
import com.sejong.chatservice.application.postlike.dto.response.LikeResponse;
import com.sejong.chatservice.application.postlike.dto.response.LikeStatusResponse;
import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.postlike.domain.PostLike;
import com.sejong.chatservice.core.postlike.domain.PostLikeCount;
import com.sejong.chatservice.core.postlike.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostInternalFacade postInternalFacade;

    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 50) // 50ms 간격 재시도
    )
    @Transactional
    public LikeResponse createLike(String userId, Long postId, PostType postType) {
        postInternalFacade.checkPost(postId,postType);
        likeRepository.validateExists(Long.valueOf(userId), postId, postType);

        PostLike postLike = PostLike.from(Long.valueOf(userId), postId, postType, LocalDateTime.now());
        PostLike savedPostLike = likeRepository.save(postLike);

        PostLikeCount postLikeCount = likeRepository.increaseLikeCount(postId, postType);
        return LikeResponse.from(savedPostLike, postLikeCount.getCount());
    }

    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 50) // 50ms 간격 재시도
    )
    @Transactional
    public LikeResponse deleteLike(String userId, Long postId, PostType postType) {
        postInternalFacade.checkPost(postId,postType);
        PostLike postLike = likeRepository.findOne(Long.valueOf(userId), postId, postType);
        Long deletedId = likeRepository.deleteById(postLike.getId());

        PostLikeCount postLikeCount = likeRepository.decreaseLikeCount(postId, postType);
        return LikeResponse.deleteFrom(deletedId, postLikeCount.getCount());
    }


    @Recover
    public LikeResponse recover(ObjectOptimisticLockingFailureException e, String userId, Long postId, PostType postType) {
        throw new ApiException(ErrorCode.BAD_REQUEST, "좋아요 요청이 너무 몰렸습니다. 잠시 후 다시 시도해주세요.");
    }

    @Transactional(readOnly = true)
    public LikeStatusResponse getLikeStatus(String userId, Long postId, PostType postType) {
        boolean isLiked = likeRepository.isExists(Long.valueOf(userId),postId,postType);
        return LikeStatusResponse.of(isLiked);
    }

    @Transactional
    public LikeCountResponse getLikeCount(Long postId, PostType postType) {

        postInternalFacade.checkPost(postId,postType);
        PostLikeCount postLikeCount = likeRepository.findLikeCount(postId, postType);
        return LikeCountResponse.of(postLikeCount.getCount());

    }
}
