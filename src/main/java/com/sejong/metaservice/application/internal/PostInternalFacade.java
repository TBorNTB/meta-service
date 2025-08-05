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
    private final ArchiveInternalService archiveInternalService;

    public void checkPostExistance(Long postId, PostType postType) {
        switch (postType) {
            case ARCHIVE -> archiveInternalService.validateExists(postId);
            case PROJECT -> projectInternalService.validateExists(postId);
            default -> throw new BaseException(BAD_REQUEST);
        }
    }
}
