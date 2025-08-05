package com.sejong.metaservice.application.view.service;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.infrastructure.redis.RedisService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewScheduler {

    private final RedisService redisService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ViewService viewService;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul") // 매일 오전 3시
    public void syncViewCount() {
        try {
            log.info("Redis 조회수 MySQL 동기화 배치 작업 시작");
            
            Set<String> viewCountKeys = redisTemplate.keys("post:*:view:count");
            
            if (viewCountKeys.isEmpty()) {
                log.info("동기화할 조회수 데이터가 없습니다.");
                return;
            }
            
            for (String key : viewCountKeys) {
                try {
                    syncSingleViewCount(key);
                } catch (Exception e) {
                    log.error("조회수 동기화 실패: key={}", key, e);
                }
            }
            
            log.info("Redis 조회수 MySQL 동기화 배치 작업 완료: {} 건 처리", viewCountKeys.size());
            
        } catch (Exception e) {
            log.error("Redis 조회수 배치 작업 실패", e);
            throw new RuntimeException("Redis 조회수 배치 작업 실패", e);
        }
    }
    
    private void syncSingleViewCount(String redisKey) {
        // Redis key 파싱: post:PROJECT:123:view:count
        String[] keyParts = redisKey.split(":");
        if (keyParts.length != 5) {
            log.warn("유효하지 않은 Redis 키 형식: {}", redisKey);
            return;
        }
        
        PostType postType = PostType.valueOf(keyParts[1]);
        Long postId = Long.valueOf(keyParts[2]);
        Long viewCount = redisService.getViewCount(redisKey);
        
        viewService.upsertViewCount(postId, postType, viewCount);
        log.debug("조회수 동기화 완료: postType={}, postId={}, viewCount={}", postType, postId, viewCount);
    }
}
