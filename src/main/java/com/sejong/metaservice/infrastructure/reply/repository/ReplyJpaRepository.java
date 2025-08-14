package com.sejong.metaservice.infrastructure.reply.repository;

import com.sejong.metaservice.infrastructure.reply.entity.ReplyEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyJpaRepository extends JpaRepository<ReplyEntity, Long> {
    @Query("""
    SELECT r FROM ReplyEntity r
    WHERE r.commentEntity.id = :commentParentId
      AND (:cursorId IS NULL OR :cursorId <= 0 OR r.id < :cursorId)
    ORDER BY r.createdAt DESC
""")
    List<ReplyEntity> findAllReplyCommentsDesc(
            @Param("commentParentId") Long commentParentId,
            @Param("cursorId") Long cursorId,
            Pageable pageable);
    @Query("""
    SELECT r FROM ReplyEntity r
    WHERE r.commentEntity.id = :commentParentId
      AND (:cursorId IS NULL OR :cursorId <= 0 OR r.id > :cursorId)
    ORDER BY r.createdAt ASC
""")
    List<ReplyEntity> findAllReplyCommentsAsc(
            @Param("commentParentId") Long commentParentId,
            @Param("cursorId") Long cursorId,
            Pageable pageable);
}