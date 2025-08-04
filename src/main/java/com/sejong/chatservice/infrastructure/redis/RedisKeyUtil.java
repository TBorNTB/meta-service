package com.sejong.chatservice.infrastructure.redis;

import com.sejong.chatservice.core.enums.PostType;

public class RedisKeyUtil {
    public static String likeKey(PostType postType, Long postId) {
        return String.format("post:%s:%d:liked_users", postType.name(), postId);
    }
}