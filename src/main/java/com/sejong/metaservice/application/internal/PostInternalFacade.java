package com.sejong.metaservice.application.internal;

import static com.sejong.metaservice.core.common.exception.ExceptionType.BAD_REQUEST;

import com.sejong.metaservice.application.meta.MetaPostCountDto;
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
    private final CSKnowledgeInternalService CSKnowledgeInternalService;

    public String checkPostExistanceAndOwner(Long postId, PostType postType) {
        switch (postType) {
            case NEWS -> {
                return newsInternalService.validateExists(postId);
            }
            case PROJECT -> {
                return projectInternalService.validateExists(postId);
            }
            case ARTICLE -> {
                return CSKnowledgeInternalService.validateExists(postId);
            }
            default -> throw new BaseException(BAD_REQUEST);

        }
    }

    public MetaPostCountDto getPostCount() {
        Long projectCount = projectInternalService.getProjectCount();
        Long csCount = CSKnowledgeInternalService.getCsCount();
        Long newsCount = newsInternalService.getNewsCount();
        return MetaPostCountDto.of(projectCount, csCount, newsCount);
    }
}
