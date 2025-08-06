package com.sejong.metaservice.infrastructure.postlike.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import com.sejong.metaservice.core.postlike.repository.LikeRepository;
import com.sejong.metaservice.infrastructure.postlike.entity.PostLikeEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public void toggleLike(PostLike postLike) {
        Optional<PostLikeEntity> postLikeEntity = likeJpaRepository.findByUserIdAndPostIdAndPostType(
                postLike.getUserId(), postLike.getPostId(), postLike.getPostType());

        if (postLikeEntity.isPresent()) {
            likeJpaRepository.deleteById(postLikeEntity.get().getId());
        } else {
            likeJpaRepository.save(PostLikeEntity.from(postLike));
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
    public boolean likedAlready(Long userId, Long postId, PostType postType) {
        return likeJpaRepository.existsByUserIdAndPostIdAndPostType(userId, postId, postType);
    }
    
    @Override
    public long countByPostIdAndPostType(Long postId, PostType postType) {
        return likeJpaRepository.countByPostIdAndPostType(postId, postType);
    }
}
