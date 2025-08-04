package com.sejong.chatservice.application.postlike.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sejong.chatservice.application.config.MockBeansConfig;
import com.sejong.chatservice.application.postlike.dto.response.LikeResponse;
import com.sejong.chatservice.application.postlike.service.LikeService;
import com.sejong.chatservice.core.enums.PostType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(LikeController.class)
@AutoConfigureMockMvc
@Import(MockBeansConfig.class)
class LikeControllerTest {
    @Autowired
    MockMvc mockMVc;

    @Autowired
    LikeService likeService;

    @Autowired
    ObjectMapper objectMapper;


    @PostMapping("/{postId}")
    public ResponseEntity<LikeResponse> createLike(
            @RequestHeader("x-user") String userId,
            @PathVariable(name="postId") Long postId,
            @RequestParam(name="postType") PostType postType
    ){
        LikeResponse response = likeService.createLike(Long.valueOf(userId), postId, postType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Test
    void 좋아요_생성이_정상적으로_작동한다() {
        //given


        //when && then
    }


}