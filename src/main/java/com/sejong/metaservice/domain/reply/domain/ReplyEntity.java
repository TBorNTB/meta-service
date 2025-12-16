package com.sejong.metaservice.domain.reply.domain;

import com.sejong.metaservice.domain.comment.domain.Comment;
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
    private Comment comment;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReplyEntity from(Reply reply, Comment comment) {

        ReplyEntity replyEntity = ReplyEntity.builder()
                .id(null)
                .content(reply.getContent())
                .comment(null)
                .username(reply.getUsername())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();

        replyEntity.assignComment(comment);
        return replyEntity;
    }

    private void assignComment(Comment comment) {
        this.comment = comment;
        comment.getReplyEntities().add(this);
    }

    public Reply toDomain() {
        return Reply.builder()
                .id(id)
                .parentCommentId(comment.getId())
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
