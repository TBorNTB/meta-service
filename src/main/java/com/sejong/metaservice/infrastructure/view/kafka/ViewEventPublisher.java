package com.sejong.metaservice.infrastructure.view.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import com.sejong.metaservice.core.view.domain.View;
import com.sejong.metaservice.infrastructure.postlike.kafka.PostLikeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViewEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic = "view";

    public void publish(PostType postType, Long postId,  Long viewCount){
        log.info("발행 시작 좋아요 postId :{}, viewCount : {}", postId, viewCount);
        ViewEvent event = ViewEvent.of(postType,postId, viewCount);
        String key = "post:" + postId;
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
