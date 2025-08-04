package com.sejong.chatservice.application.internal;

import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import com.sejong.chatservice.infrastructure.client.ProjectClient;
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
        if (t instanceof ApiException) {
            throw (ApiException) t;
        }
        throw new ApiException(ErrorCode.External_Server_Error, "잠시 서비스 이용이 불가합니다.");
    }

}
