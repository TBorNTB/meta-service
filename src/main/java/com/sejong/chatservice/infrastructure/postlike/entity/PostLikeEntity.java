package com.sejong.chatservice.infrastructure.postlike.entity;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.postlike.domain.PostLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "postlike",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_post_type", columnNames = {"userId", "postId", "postType"})
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private PostType postType;
    private LocalDateTime createdAt;

    public static PostLikeEntity from(PostLike postLike) {
        return PostLikeEntity.builder()
                .id(null)
                .userId(postLike.getUserId())
                .postId(postLike.getPostId())
                .postType(postLike.getPostType())
                .createdAt(postLike.getCreatedAt())
                .build();
    }

    public PostLike toDomain() {
        return PostLike.builder()
                .id(getId())
                .userId(getUserId())
                .postId(getPostId())
                .postType(getPostType())
                .createdAt(getCreatedAt())
                .build();
    }

}
