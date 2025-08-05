package com.sejong.metaservice.application.internal;

import static com.sejong.metaservice.core.common.exception.ExceptionType.BAD_REQUEST;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostInternalFacade {

    private final ProjectInternalService projectInternalService;
    private final NewsInternalService newsInternalService;
    private final ArticleInternalService articleInternalService;

    public void checkPostExistance(Long postId, PostType postType) {
        switch (postType) {
            case NEWS -> newsInternalService.validateExists(postId);
            case PROJECT -> projectInternalService.validateExists(postId);
            case ARTICLE -> articleInternalService.validateExists(postId);
            default -> throw new BaseException(BAD_REQUEST);
        }
    }
}
