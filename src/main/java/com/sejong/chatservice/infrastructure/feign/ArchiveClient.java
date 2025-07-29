package com.sejong.chatservice.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="archive-service", path="/internal/archive")
public interface ArchiveClient {

    @GetMapping("/check/{archiveId}")
    ResponseEntity<Boolean> checkArchive(@PathVariable("archiveId") Long archiveId);
}
