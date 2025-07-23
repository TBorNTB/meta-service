package com.sejong.chatservice.application.comment.dto.response;

import com.sejong.chatservice.core.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long commentId;
    private String message;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .message("성공적으로 저장되었습니다.")
                .build();
    }

    public static CommentResponse updateFrom(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .message("수정이 완료되었습니다.")
                .build();
    }

    public static CommentResponse deleteFrom(Long deletedId) {
        return CommentResponse.builder()
                .commentId(deletedId)
                .message("삭제가 완료되었습니다.")
                .build();
    }
}
