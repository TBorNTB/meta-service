package com.sejong.metaservice.domain.reply.repository;

import com.sejong.metaservice.domain.reply.domain.Reply;
import com.sejong.metaservice.support.common.pagination.CursorPageRequest;
import java.util.List;

public interface ReplyRepository {
    Reply save(Reply reply);

    List<Reply> findAllReplyComments(Long commentParentId, CursorPageRequest cursorPageRequest);

    Reply findById(Long replyId);

    Reply updateReply(Reply updatedReply);

    Long deleteById(Long replyId);
}
