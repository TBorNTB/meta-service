package com.sejong.chatservice.infrastructure.comment.repository;

import com.sejong.chatservice.core.comment.domain.Comment;
import com.sejong.chatservice.core.comment.repository.CommentRepository;
import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.infrastructure.comment.entity.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c where c.postId = :postId AND c.postType = :postType "+
    "AND (:cursor IS NULL OR c.createdAt < :cursor) "+
    "ORDER BY c.createdAt DESC")
    List<CommentEntity> findAllComments( @Param("postId") Long postId,
                                         @Param("postType") PostType postType,
                                         @Param("cursor") LocalDateTime cursor,
                                         Pageable pageable);



}
