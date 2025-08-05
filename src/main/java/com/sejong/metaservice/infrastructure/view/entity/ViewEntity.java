package com.sejong.metaservice.infrastructure.view.entity;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.view.domain.View;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(
        name = "view",
        uniqueConstraints = {@UniqueConstraint(name = "uk_post", columnNames = {"postType","postId"})}
)
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ViewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    private Long postId;

    private Long viewCount;

    public static ViewEntity of(View view) {
    return ViewEntity.builder()
            .id(view.getId())
            .postType(view.getPostType())
            .postId(view.getPostId())
            .viewCount(view.getViewCount())
            .build();
    }

    public View toDomain() {
        return View.builder()
                .id(id)
                .postType(postType)
                .postId(postId)
                .viewCount(viewCount)
                .build();
    }

    public void updateViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
