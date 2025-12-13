package com.sejong.metaservice.application.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sejong.metaservice.application.comment.dto.request.CommentRequest;
import com.sejong.metaservice.application.comment.dto.response.CommentResponse;
import com.sejong.metaservice.application.comment.service.CommentService;
import com.sejong.metaservice.application.config.MockBeansConfig;
import com.sejong.metaservice.application.fixture.CommentFixture;
import com.sejong.metaservice.core.comment.command.CommentCommand;
import com.sejong.metaservice.core.comment.command.ShowCursorCommentCommand;
import com.sejong.metaservice.core.comment.domain.Comment;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.pagination.CursorPageResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
@Import(MockBeansConfig.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CommentService commentService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 댓글_작성을_성공적으로_수행한다() throws Exception {
        // given
        String userId ="1";
        Long postId = 1L;
        PostType postType = PostType.PROJECT;
        CommentRequest request = new CommentRequest("테스트댓글입니다.");
        Comment mockComment = CommentFixture.getComment(1L);
        CommentResponse response = CommentResponse.from(mockComment);
        when(commentService.createComment(any(CommentCommand.class))).thenReturn(response);

        // when && then
        mockMvc.perform(post("/api/comment/{postId}",postId)
                .header("X-User-Id",userId)
                .param("postType",postType.name())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(response.getCommentId()))
                .andExpect(jsonPath("$.message").value("성공적으로 저장되었습니다."));;
    }

    @Test
    void 모든_댓글조회를_정상적으로_조회한다() throws Exception {
        //given
        Long postId =1L;
        PostType postType = PostType.PROJECT;
        int size = 5;
        LocalDateTime cursor = LocalDateTime.now();
        ShowCursorCommentCommand mockCommand = ShowCursorCommentCommand.of(postId, postType, size, cursor);
        CursorPageResponse<Comment> response = CommentFixture.getPageResponse();
        when(commentService.getComments(mockCommand)).thenReturn(response);

        //when && then
        mockMvc.perform(get("/api/comment/{postId}",postId)
                .param("postType",postType.name())
                .param("cursor",cursor.toString())
                .param("size",Integer.toString(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("테스트댓글입니다."));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable(name = "commentId") Long commentId,
            @Valid @RequestBody CommentRequest request
    ) {

        CommentResponse response = commentService.updateComment(userId, commentId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Test
    void 댓글을_정상적으로_업데이트한다() throws Exception {
        // given
        String userId = "1";
        Long commentId = 1L;
        CommentRequest request = new CommentRequest("테스트댓글입니다.");
        Comment mockComment = CommentFixture.getComment(1L);
        CommentResponse response = CommentResponse.updateFrom(mockComment);
        when(commentService.updateComment(userId,commentId,request)).thenReturn(response);

        // when && then
        mockMvc.perform(patch("/api/comment/{commentId}",commentId)
                .header("X-User-Id",userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("수정이 완료되었습니다."));
    }

    @Test
    void 댓글을_정상적으로_삭제한다() throws Exception {
        //given
        String userId = "1";
        Long commentId = 1L;
        CommentResponse response = CommentResponse.deleteFrom(1L);
        when(commentService.deleteComment(userId,commentId)).thenReturn(response);
        //when && then

        mockMvc.perform(delete("/api/comment/{commentId}",commentId)
                .header("X-User-Id",userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("삭제가 완료되었습니다."));
    }
}
