package com.sejong.metaservice.application.config;

import static org.mockito.Mockito.mock;

import com.sejong.metaservice.application.comment.service.CommentService;
import com.sejong.metaservice.application.postlike.service.LikeService;
import org.springframework.context.annotation.Bean;

public class MockBeansConfig {

    @Bean
    public CommentService commentService() {
        return mock(CommentService.class);
    }

    @Bean
    public LikeService likeService() {
        return mock(LikeService.class);
    }
}

