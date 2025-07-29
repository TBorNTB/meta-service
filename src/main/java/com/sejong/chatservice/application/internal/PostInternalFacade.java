package com.sejong.chatservice.application.external;

import com.sejong.chatservice.core.enums.PostType;
import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostExternalFacade {

    private final ProjectExternalService projectExternalService;
    private final ArchiveExternalService archiveExternalService;

    public void checkPost(Long postId, PostType postType) {
        switch (postType) {
            case ARCHIVE -> archiveExternalService.validateExists(postId);
            case PROJECT -> projectExternalService.validateExists(postId);
            default -> throw new ApiException(ErrorCode.BAD_REQUEST,"지원하지 않는 PostType입니다: " + postType);
        }
    }
}
