package com.sejong.metaservice.application.view.controller;

import com.sejong.metaservice.application.view.dto.response.ViewCountResponse;
import com.sejong.metaservice.application.view.service.ViewService;
import com.sejong.metaservice.core.common.enums.PostType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/view")
@RequiredArgsConstructor
public class ViewController {
    
    private final ViewService viewService;

    // TODO: 포스트 발행 시 초기화 (postType, postId, viewCount = 0) 필수
    // TODO: redis 캐시 미스 시 mysql에서 viewCount를 조회해 오기 때문.
    // TODO: redis -> mysql 동기화가 upsert(x), update(o) 임.

    @PostMapping("")
    public ResponseEntity<ViewCountResponse> initializeViewCount(
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "postType") PostType postType
    ) {
        // 이건 saga ? 프론트쪽에서 병렬적으로 api 요청 ?
        ViewCountResponse response = viewService.initializeViewCount(postId, postType);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ViewCountResponse> increaseViewCount(
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "postType") PostType postType,
            HttpServletRequest request
    ) {
        String clientIp = getClientIp(request);
        ViewCountResponse response = viewService.increaseViewCount(postId, postType, clientIp);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{postId}/count")
    public ResponseEntity<ViewCountResponse> getViewCount(
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "postType") PostType postType
    ) {
        ViewCountResponse response = viewService.getViewCount(postId, postType);
        return ResponseEntity.ok(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}