package com.sejong.metaservice.application.internal;

import static com.sejong.metaservice.core.common.exception.ExceptionType.BAD_REQUEST;
import static com.sejong.metaservice.core.common.exception.ExceptionType.EXTERNAL_SERVER_ERROR;

import com.sejong.metaservice.core.common.exception.BaseException;
import com.sejong.metaservice.infrastructure.client.ProjectClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectInternalService {
    private final ProjectClient projectClient;

    @CircuitBreaker(name = "myFeignClient", fallbackMethod = "validateExistsFallback")
    public void validateExists(Long postId) {
        log.info("시작...");
        ResponseEntity<Boolean> response = projectClient.checkProject(postId);
        if (Boolean.FALSE.equals(response.getBody())) {
            log.info("Project 검증 실패");
            throw new BaseException(BAD_REQUEST);
        }
    }

    private void validateExistsFallback(Long postId, Throwable t) {
        if (t instanceof BaseException) {
            throw (BaseException) t;
        }
        throw new BaseException(EXTERNAL_SERVER_ERROR);
    }

}
