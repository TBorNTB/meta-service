package com.sejong.chatservice.infrastructure.postlike.entity;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.postlike.domain.PostLikeCount;
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private PostType postType;
    private Long count;

    @Version
    private Long version; // 낙관적 락용

    public static PostLikeCountEntity of(Long postId, PostType postType, Long count) {
        return PostLikeCountEntity.builder()
                .id(null)
                .postId(postId)
                .postType(postType)
                .count(count)
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

    public void increment() {
        this.count++;
    }

    public void decrement() {
        this.count--;
    }
}
