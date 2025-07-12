package com.sejong.chatservice.infrastructure.reply;

import com.sejong.chatservice.core.reply.Reply;
import com.sejong.chatservice.infrastructure.comment.CommentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private void assignComment(CommentEntity commentEntity) {
        this.commentEntity = commentEntity;
        commentEntity.getReplyEntities().add(this);
    }

    public static ReplyEntity from(Reply reply, CommentEntity commentEntity) {

        ReplyEntity replyEntity = ReplyEntity.builder()
                .id(null)
                .content(reply.getContent())
                .commentEntity(null)
                .userId(reply.getUserId())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();

        replyEntity.assignComment(commentEntity);
        return replyEntity;
    }

    public Reply toDomain() {
        return Reply.builder()
                .id(id)
                .content(content)
                .userId(userId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
