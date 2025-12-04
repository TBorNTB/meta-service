package com.sejong.metaservice.application.meta;

import com.sejong.metaservice.application.internal.PostInternalFacade;
import com.sejong.metaservice.application.internal.ProjectInternalService;
import com.sejong.metaservice.application.internal.UserInternalService;
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
