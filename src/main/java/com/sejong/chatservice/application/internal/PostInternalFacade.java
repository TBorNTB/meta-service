package com.sejong.chatservice.application.internal;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostInternalFacade {

    private final ProjectInternalService projectInternalService;
    private final ArchiveInternalService archiveInternalService;

    public void checkPost(Long postId, PostType postType) {
        switch (postType) {
            case ARCHIVE -> archiveInternalService.validateExists(postId);
            case PROJECT -> projectInternalService.validateExists(postId);
            default -> throw new ApiException(ErrorCode.BAD_REQUEST,"지원하지 않는 PostType입니다: " + postType);
        }
    }
}
