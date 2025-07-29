package com.sejong.chatservice.infrastructure.reply.repository;

import com.sejong.chatservice.core.common.pagination.Cursor;
import com.sejong.chatservice.core.common.pagination.CursorPageRequest;
import com.sejong.chatservice.core.common.pagination.PageSearchCommand;
import com.sejong.chatservice.core.common.pagination.enums.SortDirection;
import com.sejong.chatservice.core.reply.domain.Reply;
import com.sejong.chatservice.core.reply.repository.ReplyRepository;
import com.sejong.chatservice.infrastructure.comment.entity.CommentEntity;
import com.sejong.chatservice.infrastructure.comment.repository.CommentJpaRepository;
import com.sejong.chatservice.infrastructure.reply.entity.ReplyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {

    private final ReplyJpaRepository replyJpaRepository;
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Reply save(Reply reply) {
        Long parentCommentId = reply.getParentCommentId();
        CommentEntity commentEntity = commentJpaRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("해당 정보가 없어"));

        ReplyEntity replyEntity = ReplyEntity.from(reply, commentEntity);

        ReplyEntity responseEntity = replyJpaRepository.save(replyEntity);
        return responseEntity.toDomain();
    }

    @Override
    public List<Reply> findAllReplyComments(Long commentParentId, CursorPageRequest cursorRequest) {
        Long cursorId = Optional.ofNullable(cursorRequest.getCursor())
                .map(Cursor::getCommentId)
                .orElse(null);

        Sort.Direction sortDirection = cursorRequest.getDirection() == SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                0,
                cursorRequest.getSize() + 1,
                Sort.by(sortDirection, cursorRequest.getSortBy())
        );

        List<ReplyEntity> replyEntities = replyJpaRepository.findAllReplyComments(commentParentId,cursorId ,pageable);

        return replyEntities.stream()
                .map(ReplyEntity::toDomain)
                .toList();
    }

    @Override
    public Reply findById(Long replyId) {
        ReplyEntity replyEntity = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("실패!"));
        return replyEntity.toDomain();
    }

    @Override
    public Reply updateReply(Reply reply) {
        ReplyEntity replyEntity = replyJpaRepository.findById(reply.getId())
                .orElseThrow(() -> new RuntimeException("실패!"));

        ReplyEntity updatedEntity = replyEntity.updateReply(reply.getContent(), reply.getUpdatedAt());
        return updatedEntity.toDomain();
    }

    @Override
    public Long deleteById(Long replyId) {
        replyJpaRepository.deleteById(replyId);
        return replyId;
    }
}
