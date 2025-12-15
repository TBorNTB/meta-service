package com.sejong.metaservice.domain.comment.repository;

import com.sejong.metaservice.domain.comment.domain.Comment;
import com.sejong.metaservice.support.common.enums.PostType;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
    SELECT c FROM Comment c
    WHERE c.postId = :postId
      AND c.postType = :postType
      AND (:cursorId IS NULL OR :cursorId <= 0 OR c.id < :cursorId)
    """)
    List<Comment> findAllCommentsDesc(
            @Param("postId") Long postId,
            @Param("postType") PostType postType,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("""
    SELECT c FROM Comment c
    WHERE c.postId = :postId
      AND c.postType = :postType
      AND (:cursorId IS NULL OR :cursorId <= 0 OR c.id > :cursorId)
    """)
    List<Comment> findAllCommentsAsc(
            @Param("postId") Long postId,
            @Param("postType") PostType postType,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

}
