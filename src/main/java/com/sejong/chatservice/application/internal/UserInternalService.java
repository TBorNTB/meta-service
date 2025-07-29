package com.sejong.chatservice.application.external;

import com.sejong.chatservice.application.comment.dto.response.CommentResponse;
import com.sejong.chatservice.core.comment.command.CommentCommand;
import com.sejong.chatservice.core.error.code.ErrorCode;
import com.sejong.chatservice.core.error.exception.ApiException;
import com.sejong.chatservice.infrastructure.feign.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserExternalService {
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
        log.info("user 서킷브레이커 작동!");
        throw new ApiException(ErrorCode.External_Server_Error, t.getMessage());
    }
}
