package com.sejong.chatservice.infrastructure.comment;

import com.sejong.chatservice.core.comment.Comment;
import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.reply.Reply;
import com.sejong.chatservice.infrastructure.reply.ReplyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private PostType postType;

    @OneToMany(mappedBy = "commentEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplyEntity> replyEntities = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentEntity from(Comment comment) {

        return CommentEntity.builder()
                .id(null)
                .content(comment.getContent())
                .userId(comment.getUserId())
                .postId(comment.getPostId())
                .postType(comment.getPostType())
                .replyEntities(new ArrayList<>())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public Comment toDomain() {
        List<Reply> replies = replyEntities.stream()
                .map(ReplyEntity::toDomain)
                .toList();

        return Comment.builder()
                .id(null)
                .content(this.content)
                .userId(this.userId)
                .postId(this.postId)
                .postType(this.postType)
                .replies(replies)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
