package com.sejong.chatservice.application.internal;

import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import com.sejong.chatservice.infrastructure.feign.ArchiveClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArchiveInternalService {

    private final ArchiveClient archiveClient;

    @CircuitBreaker(name = "myFeignClient", fallbackMethod = "validateExistsFallback")
    public void validateExists(Long archiveId) {
        ResponseEntity<Boolean> response = archiveClient.checkArchive(archiveId);
        log.info("response: {}",response.getBody());
        if (Boolean.FALSE.equals(response.getBody())) {
            log.info("Archive 검증 실패");
            throw new ApiException(ErrorCode.BAD_REQUEST,"해당 Archive Id가 없습니다.");
        }
    }

    private void validateExistsFallback(Long archiveId, Throwable t) {
        String message = t.getMessage().startsWith("해당") ? t.getMessage() : "Archive Server 에러";
        throw new ApiException(ErrorCode.External_Server_Error, message);
    }
}
