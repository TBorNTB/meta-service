package com.sejong.chatservice.core.postlike.repository;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.postlike.domain.PostLike;
import com.sejong.chatservice.core.postlike.domain.PostLikeCount;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {
    PostLike save(PostLike postLike);

    void validateExists(Long aLong, Long postId, PostType postType);

    PostLikeCount increaseLikeCount(Long postId, PostType postType);

    PostLikeCount decreaseLikeCount(Long postId, PostType postType);

    Optional<PostLike> findOne(Long aLong, Long postId, PostType postType);

    Long deleteById(Long id);

    boolean isExists(Long aLong, Long postId, PostType postType);

    PostLikeCount findLikeCount( Long postId, PostType postType);

    List<PostLike> findAll();
}
