package com.sejong.chatservice.infrastructure.reply.repository;

import com.sejong.chatservice.infrastructure.reply.entity.ReplyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyJpaRepository extends JpaRepository<ReplyEntity, Long> {
    @Query("SELECT r FROM ReplyEntity r WHERE r.commentEntity.id = :commentParentId AND " +
            "(:cursorId IS NULL OR :cursorId <= 0 OR r.id < :cursorId) ORDER BY r.createdAt DESC ")
    List<ReplyEntity> findAllReplyComments(
            @Param("commentParentId")Long commentParentId,
            @Param("cursorId")Long cursorId,
            Pageable pageable);
}