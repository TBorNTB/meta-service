package com.sejong.metaservice.infrastructure.postlike.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.infrastructure.postlike.entity.PostLikeEntity;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeJpaRepository extends JpaRepository<PostLikeEntity, Long> {
    Optional<PostLikeEntity> findByUserIdAndPostIdAndPostType(Long userId, Long postId, PostType postType);

    boolean existsByUserIdAndPostIdAndPostType(Long userId, Long postId, PostType postType);

    long countByPostIdAndPostType(Long postId, PostType postType);
    
    @Query("SELECT CONCAT('post:', p.postType, ':', p.postId, ':like:count'), COUNT(p) " +
           "FROM PostLikeEntity p GROUP BY p.postType, p.postId")
    Map<String, Long> getLikeCountStatistics();
}
