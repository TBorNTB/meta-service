package com.sejong.chatservice.core.reply.repository;

import com.sejong.chatservice.core.common.PageSearchCommand;
import com.sejong.chatservice.core.reply.domain.Reply;
import java.util.List;

public interface ReplyRepository {
    Reply save(Reply reply);

    List<Reply> findAllReplyComments(Long commentParentId,PageSearchCommand command);

    Reply findById(Long replyId);

    Reply updateReply(Reply updatedReply);

    Long deleteById(Long replyId);
}
