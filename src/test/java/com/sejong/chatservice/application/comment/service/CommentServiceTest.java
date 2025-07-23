package com.sejong.chatservice.application.comment.service;

import com.sejong.chatservice.application.comment.command.CommentCommand;
import com.sejong.chatservice.application.comment.command.ShowCursorCommentCommand;
import com.sejong.chatservice.application.comment.dto.request.CommentRequest;
import com.sejong.chatservice.application.comment.dto.response.CommentResponse;
import com.sejong.chatservice.application.fixture.CommentFixture;
import com.sejong.chatservice.core.comment.domain.Comment;
import com.sejong.chatservice.core.comment.repository.CommentRepository;
import com.sejong.chatservice.core.common.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository);
    }

    @Test
    void 댓글을_생성한다() {
        // given
        CommentCommand command = CommentFixture.getCommentCommand();
        Comment comment = CommentFixture.getComment(2L);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        // when
        CommentResponse response = commentService.createComment(command);

        // then
        assertThat(response.getMessage()).isEqualTo("성공적으로 저장되었습니다.");
    }

    @Test
    void 모든_댓글을_조회한다() {
        // given

        ShowCursorCommentCommand command = CommentFixture.getShowCursorCommentCommand();
        Comment comment1 = CommentFixture.getComment(1L);
        Comment comment2 = CommentFixture.getComment(2L);
        List<Comment> comments = List.of(comment1, comment2);
        Pageable pageable = PageRequest.of(0, command.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        when(commentRepository.findAllComments(command.getPostId(),command.getPostType(),command.getCursor(),pageable))
                .thenReturn(comments);

        // when
        PageResponse<Comment> response = commentService.getComments(command);

        // then
        assertThat(response.getContent()).isEqualTo(comments);
    }

    @Test
    void 댓글을_갱신한다() {
        // given
        String userId = "1";
        Long commentId = 1L;
        CommentRequest request = new CommentRequest("테스트댓글입니다.");
        Comment comment = CommentFixture.getComment(1L);
        when(commentRepository.findByCommentId(commentId)).thenReturn(comment);
        when(commentRepository.updateComment(any(Comment.class))).thenReturn(comment);
        // when
        CommentResponse response = commentService.updateComment(userId, commentId, request);

        // then
        assertThat(response.getMessage()).isEqualTo("수정이 완료되었습니다.");
    }

    @Transactional
    public CommentResponse deleteComment(String userId, Long commentId) {
        //todo userId를 통해 실제로 해당 유저가 있는지 조회하는 코드 작성 만약 일치하지 않으면 오류반환
        Comment comment = commentRepository.findByCommentId(commentId);
        comment.validateUserId(Long.valueOf(userId));

        Long deletedId = commentRepository.deleteComment(commentId);
        return CommentResponse.deleteFrom(deletedId);
    }

    @Test
    void 댓글을_삭제한다() {
        // given
        String userId = "1";
        Long commentId = 1L;
        Comment realComment = CommentFixture.getComment(1L);
        Comment comment = spy(realComment);
        Long deleteId = 1L;
        when(commentRepository.findByCommentId(commentId)).thenReturn(comment);
        when(commentRepository.deleteComment(commentId)).thenReturn(deleteId);

        // when
        CommentResponse response = commentService.deleteComment(userId, commentId);

        // then
        assertThat(response.getMessage()).isEqualTo("삭제가 완료되었습니다.");
        assertThat(response.getCommentId()).isEqualTo(deleteId);
        verify(comment).validateUserId(Long.valueOf(userId));

    }

}