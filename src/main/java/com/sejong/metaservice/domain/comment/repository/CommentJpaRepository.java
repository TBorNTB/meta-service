package com.sejong.metaservice.domain.comment.repository;

import com.sejong.metaservice.domain.comment.domain.CommentEntity;
import com.sejong.metaservice.support.common.enums.PostType;
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
