package com.sejong.metaservice.application.view.service;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.application.view.dto.response.ViewCountResponse;
import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.view.domain.View;
import com.sejong.metaservice.core.view.repository.ViewRepository;
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
    private final ViewRepository viewRepository;

    @Transactional
    public ViewCountResponse initializeViewCount(Long postId, PostType postType) {
        View view = View.of(postType, postId, 0L);
        View savedView = viewRepository.save(view);
        return ViewCountResponse.of(savedView.getViewCount());
    }


    @Transactional
    public void updateViewCount(Long postId, PostType postType, Long viewCount) {
        View view = View.of(postType, postId, viewCount);
        viewRepository.updateViewCount(view);
    }


    public void checkPostExistence(Long postId, PostType postType) {
        postInternalFacade.checkPostExistance(postId, postType);
    }


    public ViewCountResponse increaseViewCount(Long postId, PostType postType, String clientIp) {
        checkPostExistence(postId, postType);
        
        String ipKey = RedisKeyUtil.viewIpKey(postType, postId, clientIp);
        String viewCountKey = RedisKeyUtil.viewCountKey(postType, postId);

        // cache miss (mysql -> redis)
        if (!redisService.hasKey(viewCountKey)) {
            View view = viewRepository.findOne(postType, postId);
            redisService.setCount(viewCountKey, view.getViewCount());
        }

        // ip already viewed
        if (redisService.hasKey(ipKey)) {
            long currentViewCount = redisService.getCount(viewCountKey);
            return ViewCountResponse.of(currentViewCount);
        }

        // register this ip
        redisService.markAsViewed(ipKey, VIEW_TTL);
        Long newViewCount = redisService.increment(viewCountKey);
        
        return ViewCountResponse.of(newViewCount);
    }


    public ViewCountResponse getViewCount(Long postId, PostType postType) {
        checkPostExistence(postId, postType);
        
        String viewCountKey = RedisKeyUtil.viewCountKey(postType, postId);

        // cache miss (mysql -> redis)
        if (!redisService.hasKey(viewCountKey)) {
            View view = viewRepository.findOne(postType, postId);
            redisService.setCount(viewCountKey, view.getViewCount());
        }

        Long viewCount = redisService.getCount(viewCountKey);
        
        return ViewCountResponse.of(viewCount);
    }
}