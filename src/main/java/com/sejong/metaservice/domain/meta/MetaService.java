package com.sejong.metaservice.domain.meta;

import com.sejong.metaservice.support.common.internal.PostInternalFacade;
import com.sejong.metaservice.support.common.internal.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetaService {
    private final UserInternalService userInternalService;
    private final PostInternalFacade postInternalFacade;

    public MetaCountResponse getMetaCountInfo() {
        Long userCount = userInternalService.getUserCount();
        MetaPostCountDto metaPostcountDto = postInternalFacade.getPostCount();
        MetaCountResponse metaCountResponse = MetaCountResponse.of(userCount, metaPostcountDto);
        return metaCountResponse;
    }
}
