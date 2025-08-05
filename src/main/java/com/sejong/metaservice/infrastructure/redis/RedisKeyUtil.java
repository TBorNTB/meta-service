package com.sejong.metaservice.infrastructure.redis;

import com.sejong.metaservice.core.enums.PostType;

public class RedisKeyUtil {
    public static String likeCountKey(PostType postType, Long postId) {
        return "post:" + postType + ":" + postId + ":count";
    }
}