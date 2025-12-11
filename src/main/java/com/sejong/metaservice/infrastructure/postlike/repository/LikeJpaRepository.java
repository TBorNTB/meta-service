package com.sejong.metaservice.infrastructure.postlike.repository;

import com.sejong.metaservice.infrastructure.postlike.entity.PostLikeEntity;
import com.sejong.metaservice.support.common.enums.PostType;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeJpaRepository extends JpaRepository<PostLikeEntity, Long> {
    Optional<PostLikeEntity> findByUsernameAndPostIdAndPostType(String username, Long postId, PostType postType);

    boolean existsByUsernameAndPostIdAndPostType(String username, Long postId, PostType postType);

    long countByPostIdAndPostType(Long postId, PostType postType);
    
    @Query("SELECT CONCAT('post:', p.postType, ':', p.postId, ':like:count'), COUNT(p) " +
           "FROM PostLikeEntity p GROUP BY p.postType, p.postId")
    Map<String, Long> getLikeCountStatistics();
}
