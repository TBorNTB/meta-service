package com.sejong.metaservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="archive-service", path="/internal/archive")
public interface NewsClient {

    @GetMapping("/check/{newsId}")
    ResponseEntity<Boolean> checkArchive(@PathVariable("newsId") Long newsId);
}
