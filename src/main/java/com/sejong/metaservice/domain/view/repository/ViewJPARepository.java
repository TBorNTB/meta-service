package com.sejong.metaservice.domain.view.repository;

import com.sejong.metaservice.domain.view.domain.ViewEntity;
import com.sejong.metaservice.support.common.enums.PostType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ViewJPARepository extends JpaRepository<ViewEntity, Long> {
    Optional<ViewEntity> findByPostTypeAndPostId(PostType postType, Long postId);
}
