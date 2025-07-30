package com.sejong.chatservice.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addUserToLikeSet(String key, Long userId) {
        redisTemplate.opsForSet().add(key, userId.toString());
    }

    public void removeUserFromLikeSet(String key, Long userId) {
        redisTemplate.opsForSet().remove(key, userId.toString());
    }

    public boolean isUserInLikeSet(String key, Long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId.toString()));
    }

    public Long getLikeCount(String key) {
        Long size = redisTemplate.opsForSet().size(key);
        return size != null ? size : 0L;
    }

    public void setLikeCount(String key, Long count) {
        redisTemplate.opsForValue().set(key, String.valueOf(count));
    }

    public void clearAllLikeKeys() {
        Set<String> keys = redisTemplate.keys("post:*:liked_users");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void setExpire(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
    }


}
