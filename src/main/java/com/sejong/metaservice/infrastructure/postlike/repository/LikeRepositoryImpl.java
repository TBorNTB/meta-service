package com.sejong.metaservice.infrastructure.postlike.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import com.sejong.metaservice.core.postlike.repository.LikeRepository;
import com.sejong.metaservice.infrastructure.postlike.entity.LikeStatus;
import com.sejong.metaservice.infrastructure.postlike.entity.PostLikeEntity;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public LikeStatus toggleLike(PostLike postLike) {
        try {
            Optional<PostLikeEntity> postLikeEntity = likeJpaRepository.findByUserIdAndPostIdAndPostType(
                    postLike.getUserId(), postLike.getPostId(), postLike.getPostType());

            if (postLikeEntity.isPresent()) {
                likeJpaRepository.deleteById(postLikeEntity.get().getId());
                return LikeStatus.UNLIKED;

            } else {

                likeJpaRepository.save(PostLikeEntity.from(postLike));
                return LikeStatus.LIKED;
            }
        } catch (DataIntegrityViolationException e) {
            // Unique 제약 조건 위반 시 - 이미 다른 요청이 저장했다는 의미 -> 다시 조회해서 삭제 처리
            Optional<PostLikeEntity> existingEntity = likeJpaRepository.findByUserIdAndPostIdAndPostType(
                    postLike.getUserId(), postLike.getPostId(), postLike.getPostType());
            existingEntity.ifPresent(postLikeEntity -> likeJpaRepository.deleteById(postLikeEntity.getId()));
            return LikeStatus.UNLIKED;
        }
    }

    @Override
    public PostLike save(PostLike postLike) {
        PostLikeEntity entity = PostLikeEntity.from(postLike);
        PostLikeEntity postLikeEntity = likeJpaRepository.save(entity);
        return postLikeEntity.toDomain();
    }

    @Override
    public Optional<PostLike> findOne(Long userId, Long postId, PostType postType) {
        return likeJpaRepository.findByUserIdAndPostIdAndPostType(userId, postId, postType)
                .map(PostLikeEntity::toDomain);
    }

    @Override
    public Long deleteById(Long id) {
        likeJpaRepository.deleteById(id);
        return id;
    }

    @Override
    public boolean liked(Long userId, Long postId, PostType postType) {
        return likeJpaRepository.existsByUserIdAndPostIdAndPostType(userId, postId, postType);
    }
    
    @Override
    public long countByPostIdAndPostType(Long postId, PostType postType) {
        return likeJpaRepository.countByPostIdAndPostType(postId, postType);
    }
    
    @Override
    public Map<String, Long> getLikeCountStatistics() {
        return likeJpaRepository.getLikeCountStatistics();
    }
}
