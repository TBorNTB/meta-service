package com.sejong.metaservice.application.reply.dto.response;

import com.sejong.metaservice.core.reply.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyCommentResponse {
    private Long replyId;
    private String message;

    public static ReplyCommentResponse from(Reply reply) {
        return ReplyCommentResponse.builder()
                .replyId(reply.getId())
                .message("성공적으로 저장되었습니다.")
                .build();
    }

    public static ReplyCommentResponse updateFrom(Reply reply) {
        return ReplyCommentResponse.builder()
                .replyId(reply.getId())
                .message("성공적으로 갱신되었습니다.")
                .build();
    }

    public static ReplyCommentResponse deleteFrom(Long replyId) {
        return ReplyCommentResponse.builder()
                .replyId(replyId
                )
                .message("성공적으로 삭제되었습니다.")
                .build();
    }
}
