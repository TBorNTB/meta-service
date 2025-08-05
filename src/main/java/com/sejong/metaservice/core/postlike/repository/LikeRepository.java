package com.sejong.metaservice.core.postlike.repository;

import com.sejong.metaservice.core.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import java.util.Optional;

public interface LikeRepository {
    PostLike save(PostLike postLike);

    Optional<PostLike> findOne(Long aLong, Long postId, PostType postType);

    Long deleteById(Long id);

    boolean likedAlready(Long userId, Long postId, PostType postType);
}
