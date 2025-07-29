package com.sejong.chatservice.infrastructure.postlike.repository;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import com.sejong.chatservice.core.postlike.domain.PostLike;
import com.sejong.chatservice.core.postlike.domain.PostLikeCount;
import com.sejong.chatservice.core.postlike.repository.LikeRepository;
import com.sejong.chatservice.infrastructure.postlike.entity.PostLikeCountEntity;
import com.sejong.chatservice.infrastructure.postlike.entity.PostLikeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;
    private final LikeCountJpaRepository likeCountJpaRepository;

    @Override
    public PostLike save(PostLike postLike) {
        PostLikeEntity entity = PostLikeEntity.from(postLike);
        PostLikeEntity postLikeEntity = likeJpaRepository.save(entity);
        return postLikeEntity.toDomain();
    }

    @Override
    public void validateExists(Long userId, Long postId, PostType postType) {
        likeJpaRepository.findByUserIdAndPostIdAndPostType(userId, postId, postType)
                .ifPresent(like -> {
                    throw new ApiException(ErrorCode.BAD_REQUEST, "이미 좋아요 눌렀잖아요");
                });
    }

    @Override
    public PostLikeCount increaseLikeCount(Long postId, PostType postType) {
        PostLikeCountEntity likeCountEntity = likeCountJpaRepository.findByPostIdAndPostType(postId, postType)
                .orElseGet(() -> PostLikeCountEntity.of(postId,postType,0L));
        likeCountEntity.increment();
        PostLikeCountEntity entity = likeCountJpaRepository.save(likeCountEntity);
        return entity.toDomain();
    }

    @Override
    public PostLikeCount decreaseLikeCount(Long postId, PostType postType) {
        PostLikeCountEntity likeCountEntity = likeCountJpaRepository.findByPostIdAndPostType(postId, postType)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "없어요!"));
        likeCountEntity.decrement();
        PostLikeCountEntity updatedEntity = likeCountJpaRepository.save(likeCountEntity);
        return updatedEntity.toDomain();
    }

    @Override
    public PostLike findOne(Long aLong, Long postId, PostType postType) {
        PostLikeEntity postLikeEntity = likeJpaRepository.findByUserIdAndPostIdAndPostType(aLong, postId, postType)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "없어요!"));
        return postLikeEntity.toDomain();
    }

    @Override
    public Long deleteById(Long id) {
        likeJpaRepository.deleteById(id);
        return id;
    }

    @Override
    public boolean isExists(Long userId, Long postId, PostType postType) {
        return likeJpaRepository.existsByUserIdAndPostIdAndPostType(userId, postId, postType);
    }

    @Override
    public PostLikeCount findLikeCount(Long postId, PostType postType) {
        PostLikeCountEntity entity = likeCountJpaRepository.findByPostIdAndPostType(postId, postType)
                .orElseGet(() -> {
                    PostLikeCountEntity newEntity = PostLikeCountEntity.of(postId, postType, 0L);
                    return likeCountJpaRepository.save(newEntity);
                });

        return entity.toDomain();
    }
}
