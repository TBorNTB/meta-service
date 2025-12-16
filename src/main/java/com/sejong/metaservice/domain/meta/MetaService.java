package com.sejong.metaservice.domain.meta;

import com.sejong.metaservice.support.common.internal.PostInternalFacade;
import com.sejong.metaservice.support.common.internal.UserInternalService;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetaService {

    private final UserInternalService userInternalService;
    private final PostInternalFacade postInternalFacade;

    // TODO: 직렬 호출
    // public MetaCountResponse getMetaCountInfo() {
    //     long start = System.currentTimeMillis();
    //
    //     Long userCount = userInternalService.getUserCount();
    //     MetaPostCountDto postCountDto = postInternalFacade.getPostCount();
    //
    //     log.info("[MetaService] 전체 조회 시간: {}ms", System.currentTimeMillis() - start);
    //     return MetaCountResponse.of(userCount, postCountDto);
    // }

    // 병렬 호출
    public MetaCountResponse getMetaCountInfo() {
        long start = System.currentTimeMillis();

        CompletableFuture<Long> userFuture = CompletableFuture
                .supplyAsync(userInternalService::getUserCount);
        CompletableFuture<MetaPostCountDto> postFuture = CompletableFuture
                .supplyAsync(postInternalFacade::getPostCount);

        CompletableFuture.allOf(userFuture, postFuture).join();

        log.info("[MetaService] 전체 조회 시간: {}ms", System.currentTimeMillis() - start);
        return MetaCountResponse.of(userFuture.join(), postFuture.join());
    }
}
