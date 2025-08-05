package com.sejong.metaservice.infrastructure.redis;

import com.sejong.metaservice.core.common.enums.PostType;

public class RedisKeyUtil {
    public static String likeCountKey(PostType postType, Long postId) {
        return "post:" + postType + ":" + postId + ":count";
    }
    
    public static String viewCountKey(PostType postType, Long postId) {
        return "post:" + postType + ":" + postId + ":view:count";
    }
    
    public static String viewIpKey(PostType postType, Long postId, String ip) {
        return "post:" + postType + ":" + postId + ":view:ip:" + ip;
    }
}