package com.sejong.metaservice.domain.view.service;

import static com.sejong.metaservice.support.common.exception.ExceptionType.NOT_FOUND_POST_TYPE_POST_ID;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.domain.view.domain.ViewEntity;
import com.sejong.metaservice.domain.view.kafka.ViewEventPublisher;
import com.sejong.metaservice.domain.view.repository.ViewJPARepository;
import com.sejong.metaservice.domain.view.response.ViewCountResponse;
import com.sejong.metaservice.infrastructure.redis.RedisKeyUtil;
import com.sejong.metaservice.infrastructure.redis.RedisService;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.exception.BaseException;
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
    private final ViewJPARepository viewJPARepository;
    private final ViewEventPublisher viewEventPublisher;

    @Transactional
    public void updateViewCount(Long postId, PostType postType, Long viewCount) {
        ViewEntity view = viewJPARepository
                .findByPostTypeAndPostId(postType, postId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POST_TYPE_POST_ID));
        view.updateViewCount(viewCount);
    }

    public void checkPostExistence(Long postId, PostType postType) {
        postInternalFacade.checkPostExistanceAndOwner(postId, postType);
    }

    public ViewCountResponse increaseViewCount(Long postId, PostType postType, String clientIp) {
        checkPostExistence(postId, postType);
        
        String ipKey = RedisKeyUtil.viewIpKey(postType, postId, clientIp);
        String viewCountKey = RedisKeyUtil.viewCountKey(postType, postId);

        // cache miss (mysql -> redis)
        if (!redisService.hasKey(viewCountKey)) {
            ViewEntity view = viewJPARepository.findByPostTypeAndPostId(postType, postId)
                    // 이렇게 하면 포스트 생성 시 초기화 로직이 필요 없음
                    .orElse(viewJPARepository.save(ViewEntity.of(postType, postId, 0L)));
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
        viewEventPublisher.publish(postType, postId ,newViewCount);
        return ViewCountResponse.of(newViewCount);
    }


    public ViewCountResponse getViewCount(Long postId, PostType postType) {
        checkPostExistence(postId, postType);
        
        String viewCountKey = RedisKeyUtil.viewCountKey(postType, postId);

        // cache miss (mysql -> redis)
        if (!redisService.hasKey(viewCountKey)) {
            ViewEntity view = viewJPARepository.findByPostTypeAndPostId(postType, postId)
                    // 이렇게 하면 포스트 생성 시 초기화 로직이 필요 없음
                    .orElse(viewJPARepository.save(ViewEntity.of(postType, postId, 0L)));
            redisService.setCount(viewCountKey, view.getViewCount());
        }

        Long viewCount = redisService.getCount(viewCountKey);
        
        return ViewCountResponse.of(viewCount);
    }
}