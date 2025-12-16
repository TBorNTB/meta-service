package com.sejong.metaservice.support.common.internal;

import static com.sejong.metaservice.support.common.exception.ExceptionType.BAD_REQUEST;

import com.sejong.metaservice.domain.meta.MetaPostCountDto;
import com.sejong.metaservice.support.common.enums.PostType;
import com.sejong.metaservice.support.common.exception.BaseException;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostInternalFacade {

    private final ProjectInternalService projectInternalService;
    private final NewsInternalService newsInternalService;
    private final CSKnowledgeInternalService CSKnowledgeInternalService;

    public String checkPostExistenceAndOwner(Long postId, PostType postType) {
        switch (postType) {
            case NEWS -> {
                return newsInternalService.validateExists(postId);
            }
            case PROJECT -> {
                return projectInternalService.validateExists(postId);
            }
            case ARTICLE -> {
                return CSKnowledgeInternalService.validateExists(postId);
            }
            default -> throw new BaseException(BAD_REQUEST);

        }
    }

    // TODO: 직렬 호출
    // public MetaPostCountDto getPostCount() {
    //     long start = System.currentTimeMillis();
    //
    //     Long projectCount = projectInternalService.getProjectCount();
    //     Long articleCount = CSKnowledgeInternalService.getCsCount();
    //     Long newsCount = projectInternalService.getCategoryCount();
    //
    //     log.info("[PostInternalFacade] 전체 조회 시간: {}ms", System.currentTimeMillis() - start);
    //     return MetaPostCountDto.of(projectCount, articleCount, newsCount);
    // }

    // 병렬 호출
    public MetaPostCountDto getPostCount() {
        long start = System.currentTimeMillis();

        CompletableFuture<Long> projectFuture = CompletableFuture
                .supplyAsync(projectInternalService::getProjectCount);
        CompletableFuture<Long> articleFuture = CompletableFuture
                .supplyAsync(CSKnowledgeInternalService::getCsCount);
        CompletableFuture<Long> newsFuture = CompletableFuture
                .supplyAsync(projectInternalService::getCategoryCount);

        CompletableFuture.allOf(projectFuture, articleFuture, newsFuture).join();

        log.info("[PostInternalFacade] 전체 조회 시간: {}ms", System.currentTimeMillis() - start);
        return MetaPostCountDto.of(
                projectFuture.join(),
                articleFuture.join(),
                newsFuture.join()
        );
    }
}
