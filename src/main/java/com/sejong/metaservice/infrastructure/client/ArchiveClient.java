package com.sejong.metaservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="archive-service", path="/internal/archive")
public interface ArchiveClient {

    @GetMapping("/check/{newsId}")
    ResponseEntity<Boolean> checkNews(@PathVariable("newsId") Long newsId);

  @GetMapping("/check/{csKnowledgeId}")
  ResponseEntity<Boolean> checkCSKnowledge(@PathVariable("csKnowledgeId") Long csKnowledgeId);
}