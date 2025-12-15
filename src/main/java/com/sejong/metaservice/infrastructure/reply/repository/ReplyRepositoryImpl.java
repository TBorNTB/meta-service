package com.sejong.metaservice.infrastructure.reply.repository;

import com.sejong.metaservice.core.reply.domain.Reply;
import com.sejong.metaservice.core.reply.repository.ReplyRepository;
import com.sejong.metaservice.domain.comment.domain.Comment;
import com.sejong.metaservice.domain.comment.repository.CommentRepository;
import com.sejong.metaservice.infrastructure.reply.entity.ReplyEntity;
import com.sejong.metaservice.support.common.pagination.CursorPageRequest;
import com.sejong.metaservice.support.common.pagination.enums.SortDirection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {

    private final ReplyJpaRepository replyJpaRepository;
    private final CommentRepository commentRepository;

    @Override
    public Reply save(Reply reply) {
        Long parentCommentId = reply.getParentCommentId();
        Comment comment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("해당 정보가 없어"));

        ReplyEntity replyEntity = ReplyEntity.from(reply, comment);

        ReplyEntity responseEntity = replyJpaRepository.save(replyEntity);
        return responseEntity.toDomain();
    }

    @Override
    public List<Reply> findAllReplyComments(Long commentParentId, CursorPageRequest cursorRequest) {
        Long cursorId = cursorRequest.getCursorId();

        Sort.Direction sortDirection = cursorRequest.getDirection() == SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                0,
                cursorRequest.getSize() + 1, // 다음 페이지 여부 판단용으로 1개 더
                Sort.by(sortDirection, cursorRequest.getSortBy())
        );

        List<ReplyEntity> replyEntities;

        if (sortDirection == Sort.Direction.ASC) {
            replyEntities = replyJpaRepository.findAllReplyCommentsAsc(commentParentId, cursorId, pageable);
        } else {
            replyEntities = replyJpaRepository.findAllReplyCommentsDesc(commentParentId, cursorId, pageable);
        }

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
