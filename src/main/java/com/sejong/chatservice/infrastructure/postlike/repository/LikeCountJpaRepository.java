package com.sejong.chatservice.infrastructure.postlike.repository;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.infrastructure.postlike.entity.PostLikeCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCountJpaRepository extends JpaRepository<PostLikeCountEntity, Long> {
    Optional<PostLikeCountEntity> findByPostIdAndPostType(Long postId, PostType postType);
}
