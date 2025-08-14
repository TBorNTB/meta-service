package com.sejong.metaservice.infrastructure.view.repository;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.infrastructure.view.entity.ViewEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ViewJPARepository extends JpaRepository<ViewEntity, Long> {
    Optional<ViewEntity> findByPostTypeAndPostId(PostType postType, Long postId);
}
