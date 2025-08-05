package com.sejong.metaservice.application.view.service;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.application.view.dto.response.ViewCountResponse;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.infrastructure.redis.RedisKeyUtil;
import com.sejong.metaservice.infrastructure.redis.RedisService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewService {
    
    private static final Duration VIEW_TTL = Duration.ofHours(24);
    
    private final PostInternalFacade postInternalFacade;
    private final RedisService redisService;

    @Transactional
    public ViewCountResponse increaseViewCount(Long postId, PostType postType, String clientIp) {
        postInternalFacade.checkPostExistance(postId, postType);
        
        String ipKey = RedisKeyUtil.viewIpKey(postType, postId, clientIp);
        String viewCountKey = RedisKeyUtil.viewCountKey(postType, postId);
        
        if (redisService.hasViewedWithinTtl(ipKey)) {
            long currentViewCount = redisService.getViewCount(viewCountKey);
            return ViewCountResponse.of(currentViewCount);
        }
        
        redisService.markAsViewed(ipKey, VIEW_TTL);
        Long newViewCount = redisService.incrementViewCount(viewCountKey);
        
        return ViewCountResponse.of(newViewCount);
    }

    @Transactional(readOnly = true)
    public ViewCountResponse getViewCount(Long postId, PostType postType) {
        postInternalFacade.checkPostExistance(postId, postType);
        
        String viewCountKey = RedisKeyUtil.viewCountKey(postType, postId);
        Long viewCount = redisService.getViewCount(viewCountKey);
        
        return ViewCountResponse.of(viewCount);
    }
}