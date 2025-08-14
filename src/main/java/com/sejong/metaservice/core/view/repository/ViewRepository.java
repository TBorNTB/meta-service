package com.sejong.metaservice.core.view.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.view.domain.View;

public interface ViewRepository {
    View save(View view);

    View updateViewCount(View view);

    // findOne 은 포스트 존재 여부 검증 뒤에 수행됩니다.
    View findOne(PostType postType, Long postId);
}
