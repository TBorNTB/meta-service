package com.sejong.metaservice.application.internal;

import com.sejong.metaservice.core.common.error.code.ErrorCode;
import com.sejong.metaservice.core.common.error.exception.ApiException;
import com.sejong.metaservice.infrastructure.client.ArchiveClient;
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
        if (t instanceof ApiException) {
            throw (ApiException) t;
        }
        throw new ApiException(ErrorCode.External_Server_Error, "잠시 서비스 이용이 불가합니다.");
    }
}
