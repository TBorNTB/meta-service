package com.sejong.metaservice.core.postlike.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import com.sejong.metaservice.infrastructure.postlike.entity.LikeStatus;
import java.util.Map;
import java.util.Optional;

public interface LikeRepository {
    LikeStatus toggleLike(PostLike postLike);

    PostLike save(PostLike postLike);

    Optional<PostLike> findOne(Long aLong, Long postId, PostType postType);

    Long deleteById(Long id);

    boolean liked(Long userId, Long postId, PostType postType);

    long countByPostIdAndPostType(Long postId, PostType postType);
    
    Map<String, Long> getLikeCountStatistics();
}
