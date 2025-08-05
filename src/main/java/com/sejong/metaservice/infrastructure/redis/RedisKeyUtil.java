package com.sejong.metaservice.infrastructure.redis;

import com.sejong.metaservice.core.common.enums.PostType;

public class RedisKeyUtil {
    public static String likeCountKey(PostType postType, Long postId) {
        return "post:" + postType + ":" + postId + ":count";
    }
}