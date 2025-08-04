package com.sejong.chatservice.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service", path = "/internal/project")
public interface ProjectClient {
    @GetMapping("/check/{postId}")
    ResponseEntity<Boolean> checkProject(@PathVariable("postId") Long postId);
}