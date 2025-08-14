package com.sejong.metaservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="article-service", path="/internal/article")
public interface ArticleClient {

    @GetMapping("/check/{articleId}")
    ResponseEntity<Boolean> checkArchive(@PathVariable("articleId") Long articleId);
}
