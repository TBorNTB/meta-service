package com.sejong.chatservice.infrastructure.postlike;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.postlike.domain.PostLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "postlike")
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
    private PostType postType;
    private LocalDateTime likedAt;

    public static PostLikeEntity from(PostLikeEntity postLike) {
        return PostLikeEntity.builder()
                .id(null)
                .userId(postLike.getUserId())
                .postId(postLike.getPostId())
                .postType(postLike.getPostType())
                .likedAt(postLike.getLikedAt())
                .build();
    }

    public PostLike toDomain() {
        return PostLike.builder()
                .id(getId())
                .userId(getUserId())
                .postId(getPostId())
                .postType(getPostType())
                .likedAt(getLikedAt())
                .build();
    }

}
