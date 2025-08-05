package com.sejong.metaservice.core.reply.repository;

import com.sejong.metaservice.core.common.pagination.CursorPageRequest;
import com.sejong.metaservice.core.reply.domain.Reply;
import java.util.List;

public interface ReplyRepository {
    Reply save(Reply reply);

    List<Reply> findAllReplyComments(Long commentParentId, CursorPageRequest cursorPageRequest);

    Reply findById(Long replyId);

    Reply updateReply(Reply updatedReply);

    Long deleteById(Long replyId);
}
