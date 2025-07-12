package com.sejong.chatservice.core.comment;

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

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private PostType postType;
    private List<Reply> replies = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
