package com.sejong.metaservice.application.internal;

import static com.sejong.metaservice.core.common.exception.ExceptionType.BAD_REQUEST;
import static com.sejong.metaservice.core.common.exception.ExceptionType.EXTERNAL_SERVER_ERROR;

import com.sejong.metaservice.core.common.exception.BaseException;
import com.sejong.metaservice.infrastructure.client.ArticleClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleInternalService {

    private final ArticleClient articleClient;

    @CircuitBreaker(name = "myFeignClient", fallbackMethod = "validateExistsFallback")
    public void validateExists(Long archiveId) {
        ResponseEntity<Boolean> response = articleClient.checkArchive(archiveId);
        log.info("response: {}",response.getBody());
        if (Boolean.FALSE.equals(response.getBody())) {
            log.info("Article 검증 실패");
            throw new BaseException(BAD_REQUEST);
        }
    }

    private void validateExistsFallback(Long archiveId, Throwable t) {
        if (t instanceof BaseException) {
            throw (BaseException) t;
        }
        throw new BaseException(EXTERNAL_SERVER_ERROR);
    }
}
