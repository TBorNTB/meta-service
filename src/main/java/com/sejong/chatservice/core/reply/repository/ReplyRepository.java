package com.sejong.chatservice.core.reply.repository;

import com.sejong.chatservice.core.common.pagination.CursorPageRequest;
import com.sejong.chatservice.core.common.pagination.PageSearchCommand;
import com.sejong.chatservice.core.reply.domain.Reply;
import java.util.List;

public interface ReplyRepository {
    Reply save(Reply reply);

    List<Reply> findAllReplyComments(Long commentParentId, CursorPageRequest cursorPageRequest);

    Reply findById(Long replyId);

    Reply updateReply(Reply updatedReply);

    Long deleteById(Long replyId);
}
