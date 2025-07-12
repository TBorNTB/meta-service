package com.sejong.chatservice.infrastructure.postlike;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.postlike.PostLikeCount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "postlikecount")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLikeCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private PostType postType;
    private Long count;

    @Version
    private Long version; // 낙관적 락용

    public static PostLikeCountEntity from(PostLikeCount postLikeCount) {
        return PostLikeCountEntity.builder()
                .id(null)
                .postId(postLikeCount.getPostId())
                .postType(postLikeCount.getPostType())
                .count(postLikeCount.getCount())
                .build();
    }

    public PostLikeCount toDomain() {
        return PostLikeCount.builder()
                .id(id)
                .postId(postId)
                .postType(postType)
                .count(count)
                .build();
    }
}
