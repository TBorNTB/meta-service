package com.sejong.metaservice.infrastructure.reply.entity;

import com.sejong.metaservice.core.reply.domain.Reply;
import com.sejong.metaservice.infrastructure.comment.entity.CommentEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reply")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentEntity commentEntity;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReplyEntity from(Reply reply, CommentEntity commentEntity) {

        ReplyEntity replyEntity = ReplyEntity.builder()
                .id(null)
                .content(reply.getContent())
                .commentEntity(null)
                .username(reply.getUsername())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();

        replyEntity.assignComment(commentEntity);
        return replyEntity;
    }

    private void assignComment(CommentEntity commentEntity) {
        this.commentEntity = commentEntity;
        commentEntity.getReplyEntities().add(this);
    }

    public Reply toDomain() {
        return Reply.builder()
                .id(id)
                .parentCommentId(commentEntity.getId())
                .content(content)
                .username(username)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public ReplyEntity updateReply(String content, LocalDateTime updatedAt) {
        this.content = content;
        this.updatedAt = updatedAt;
        return this;
    }
}
