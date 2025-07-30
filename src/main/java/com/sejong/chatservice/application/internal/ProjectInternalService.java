package com.sejong.chatservice.application.internal;

import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import com.sejong.chatservice.infrastructure.feign.ProjectClient;
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
            throw new ApiException(ErrorCode.BAD_REQUEST,"해당 Project Id가 없습니다.");
        }
    }

    private void validateExistsFallback(Long postId, Throwable t) {
        String message = t.getMessage().startsWith("해당") ? t.getMessage() : "Project Server 에러";
        throw new ApiException(ErrorCode.External_Server_Error, message);
    }

}
