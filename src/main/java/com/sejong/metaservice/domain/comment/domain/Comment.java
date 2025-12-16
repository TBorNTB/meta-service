package com.sejong.metaservice.domain.comment.domain;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String content;
    private String username;
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder.Default
    private int depth = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String content, LocalDateTime updatedAt) {
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public void validateWriter(String username) {
        if (!this.username.equals(username)) {
            throw new BaseException(ExceptionType.BAD_REQUEST);
        }
    }
}
