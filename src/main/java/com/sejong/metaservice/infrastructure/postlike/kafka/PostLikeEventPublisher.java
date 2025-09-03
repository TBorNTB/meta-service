package com.sejong.metaservice.infrastructure.postlike.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic = "aws.post.cdc.likes.0";

    public void publish(PostLike postLike, Long postCount){
        log.info("발행 시작 좋아요 postLike :{}, postCount : {}", postLike, postCount);
        PostLikeEvent event = PostLikeEvent.of(postLike.getPostType(),postLike.getPostId(), postCount);
        String key = "post:" + postLike.getPostId();
        kafkaTemplate.send(topic,key, toJsonString(event));
    }

    private String toJsonString(Object object) {
        try {
            String message = objectMapper.writeValueAsString(object);
            return message;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json 직렬화 실패");
        }
    }
}
