package com.sejong.metaservice.infrastructure.comment.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.infrastructure.comment.entity.CommentEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    @Query("""
    SELECT c FROM CommentEntity c
    WHERE c.postId = :postId
      AND c.postType = :postType
      AND (:cursorId IS NULL OR :cursorId <= 0 OR c.id < :cursorId)
    """)
    List<CommentEntity> findAllCommentsDesc(
            @Param("postId") Long postId,
            @Param("postType") PostType postType,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("""
    SELECT c FROM CommentEntity c
    WHERE c.postId = :postId
      AND c.postType = :postType
      AND (:cursorId IS NULL OR :cursorId <= 0 OR c.id > :cursorId)
    """)
    List<CommentEntity> findAllCommentsAsc(
            @Param("postId") Long postId,
            @Param("postType") PostType postType,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

}
