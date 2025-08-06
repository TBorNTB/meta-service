package com.sejong.metaservice.infrastructure.view.repository;

import static com.sejong.metaservice.core.common.exception.ExceptionType.NOT_FOUND_POST_TYPE_POST_ID;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.common.exception.BaseException;
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
    public View save(View view) {
        ViewEntity viewEntity = ViewEntity.of(view);
        ViewEntity savedViewEntity = viewJPARepository.save(viewEntity);
        return savedViewEntity.toDomain();
    }

    @Override
    public View updateViewCount(View view) {
        ViewEntity viewEntity = ViewEntity.of(view);
        ViewEntity foundedViewEntity = viewJPARepository
                .findByPostTypeAndPostId(view.getPostType(), view.getPostId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_POST_TYPE_POST_ID));
        foundedViewEntity.updateViewCount(view.getViewCount());
        return foundedViewEntity.toDomain();
    }

    @Override
    public View findOne(PostType postType, Long postId) {
        return viewJPARepository.findByPostTypeAndPostId(postType, postId)
                .map(ViewEntity::toDomain)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POST_TYPE_POST_ID));
    }
}
