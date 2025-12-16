package com.sejong.metaservice.client;

import com.sejong.metaservice.support.common.internal.response.PostLikeCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service", path = "/internal/project")
public interface ProjectClient {
    @GetMapping("/check/{postId}")
    ResponseEntity<PostLikeCheckResponse> checkProject(@PathVariable("postId") Long postId);

    @GetMapping("/count")
    ResponseEntity<Long> getProjectCount();
}