package com.sejong.metaservice.core.view.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.view.domain.View;

public interface ViewRepository {
    View upsert(View view);

    View findOne(PostType postType, Long postId);
}
