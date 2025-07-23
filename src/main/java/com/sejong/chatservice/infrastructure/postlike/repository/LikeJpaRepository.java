package com.sejong.chatservice.infrastructure.postlike.repository;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.infrastructure.postlike.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeJpaRepository extends JpaRepository<PostLikeEntity, Long> {
    Optional<PostLikeEntity> findByUserIdAndPostIdAndPostType(Long userId, Long postId, PostType postType);

    boolean existsByUserIdAndPostIdAndPostType(Long userId, Long postId, PostType postType);
}
