package com.sejong.metaservice.infrastructure.view.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.view.domain.View;
import com.sejong.metaservice.core.view.repository.ViewRepository;
import com.sejong.metaservice.infrastructure.view.entity.ViewEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ViewRepositoryImpl implements ViewRepository {

    private final ViewJPARepository viewJPARepository;

    @Override
    public View upsert(View view) {
        ViewEntity viewEntity = ViewEntity.of(view);
        ViewEntity foundedViewEntity = viewJPARepository
                .findByPostTypeAndPostId(view.getPostType(), view.getPostId()).orElseGet(() -> viewJPARepository.save(viewEntity));
        foundedViewEntity.updateViewCount(view.getViewCount());
        return foundedViewEntity.toDomain();
    }

    @Override
    public View findOne(PostType postType, Long postId) {
        return viewJPARepository.findByPostTypeAndPostId(postType, postId)
                .map(ViewEntity::toDomain)
                .orElse(null);
    }
}
