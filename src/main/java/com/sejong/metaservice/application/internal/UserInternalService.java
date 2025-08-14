package com.sejong.metaservice.application.internal;

import static com.sejong.metaservice.core.common.exception.ExceptionType.BAD_REQUEST;
import static com.sejong.metaservice.core.common.exception.ExceptionType.EXTERNAL_SERVER_ERROR;

import com.sejong.metaservice.core.common.exception.BaseException;
import com.sejong.metaservice.infrastructure.client.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInternalService {
    private final UserClient userClient;;

    @CircuitBreaker(name = "myFeignClient", fallbackMethod = "validateExistsFallback")
    public void validateExists(Long userId) {
        ResponseEntity<Boolean> response = userClient.getUser(userId);
        if (Boolean.FALSE.equals(response.getBody())) {
            log.info("User 검증 성공");
            throw new BaseException(BAD_REQUEST);
        }
    }

    private void validateExistsFallback(Long userId, Throwable t) {
        if (t instanceof BaseException) {
            throw (BaseException) t;
        }
        throw new BaseException(EXTERNAL_SERVER_ERROR);
    }
}
