package com.sejong.metaservice.domain.comment.domain;

import com.sejong.metaservice.domain.comment.command.CommentCommand;
import com.sejong.metaservice.domain.comment.dto.response.CommentRes;
import com.sejong.metaservice.infrastructure.reply.entity.ReplyEntity;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.exception.BaseException;
import com.sejong.metaservice.support.common.exception.ExceptionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String username;
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private PostType postType;

    @OneToMany(mappedBy = "commentEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplyEntity> replyEntities = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentEntity of(CommentCommand command) {
        return CommentEntity.builder()
                .id(null)
                .content(command.getContent())
                .username(command.getUsername())
                .postId(command.getPostId())
                .postType(command.getPostType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static CommentEntity from(CommentRes commentRes) {

        return CommentEntity.builder()
                .id(null)
                .content(commentRes.getContent())
                .username(commentRes.getUsername())
                .postId(commentRes.getPostId())
                .postType(commentRes.getPostType())
                .replyEntities(new ArrayList<>())
                .createdAt(commentRes.getCreatedAt())
                .updatedAt(commentRes.getUpdatedAt())
                .build();
    }

    public void update(String content, LocalDateTime updatedAt) {
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public void isWrittenBy(String username) {
        if (!this.username.equals(username)) {
            throw new BaseException(ExceptionType.BAD_REQUEST);
        }
    }
}
