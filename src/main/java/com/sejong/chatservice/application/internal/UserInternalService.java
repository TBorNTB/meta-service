package com.sejong.chatservice.application.internal;

import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import com.sejong.chatservice.infrastructure.client.UserClient;
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
            throw new ApiException(ErrorCode.BAD_REQUEST,"User Id가 데이터베이스에 존재하지 않습니다.");
        }
    }

    private void validateExistsFallback(Long userId, Throwable t) {
        if (t instanceof ApiException) {
            throw (ApiException) t;
        }
        throw new ApiException(ErrorCode.External_Server_Error, "잠시 서비스 이용이 불가합니다.");
    }
}
