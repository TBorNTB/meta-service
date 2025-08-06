package com.sejong.metaservice.infrastructure.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void removeUserFromLikeSet(String key, Long userId) {
        redisTemplate.opsForSet().remove(key, userId.toString());
    }

    public Long getLikeCount(String key) {
        String count = redisTemplate.opsForValue().get(key);
        return Long.parseLong(count);
    }

    public void setLikeCount(String key, Long count) {
        redisTemplate.opsForValue().set(key, String.valueOf(count));
    }

    public Long increment(String key) {
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, "0");
        }
        return redisTemplate.opsForValue().increment(key);
    }

    public Long decrement(String key) {
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, "0");
        }
        return redisTemplate.opsForValue().decrement(key);
    }

    public void clearAllLikeKeys() {
        try (org.springframework.data.redis.core.Cursor<String> cursor = redisTemplate.scan(
                org.springframework.data.redis.core.ScanOptions.scanOptions()
                    .match("post:*:like:count")
                    .count(100)
                    .build())) {
                    
            while (cursor.hasNext()) {
                redisTemplate.delete(cursor.next());
            }
        } catch (Exception e) {
            throw new RuntimeException("좋아요 키 삭제 실패", e);
        }
    }

    public boolean hasViewCount(String viewCountKey) {
        return redisTemplate.hasKey(viewCountKey);
    }

    public void setViewCount(String viewCountKey, Long count) {
        redisTemplate.opsForValue().set(viewCountKey, String.valueOf(count));
    }


    public boolean hasViewedWithinTtl(String ipKey) {
        return redisTemplate.hasKey(ipKey);
    }

    public void markAsViewed(String ipKey, Duration ttl) {
        redisTemplate.opsForValue().set(ipKey, "viewed", ttl);
    }

    public Long incrementViewCount(String viewCountKey) {
        if (!redisTemplate.hasKey(viewCountKey)) {
            redisTemplate.opsForValue().set(viewCountKey, "0");
        }
        return redisTemplate.opsForValue().increment(viewCountKey);
    }

    public Long getViewCount(String viewCountKey) {
        if (!redisTemplate.hasKey(viewCountKey)) {
            redisTemplate.opsForValue().set(viewCountKey, "0");
        }
        String count = redisTemplate.opsForValue().get(viewCountKey);
        return count == null ? 0L : Long.parseLong(count);
    }

    public void deleteViewCount(String viewCountKey) {
        redisTemplate.delete(viewCountKey);
    }
}
