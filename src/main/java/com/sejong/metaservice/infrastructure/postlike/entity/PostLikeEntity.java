package com.sejong.metaservice.infrastructure.postlike.entity;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
