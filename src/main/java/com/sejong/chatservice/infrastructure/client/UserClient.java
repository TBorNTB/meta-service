package com.sejong.chatservice.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/users")
public interface UserClient {
    @GetMapping("/check/{userId}")
    ResponseEntity<Boolean> getUser(@PathVariable("userId") Long userId);
}
