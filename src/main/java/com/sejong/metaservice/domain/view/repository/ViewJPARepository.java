package com.sejong.metaservice.domain.view.repository;

import com.sejong.metaservice.domain.view.domain.View;
import com.sejong.metaservice.support.common.enums.PostType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ViewJPARepository extends JpaRepository<View, Long> {
    Optional<View> findByPostTypeAndPostId(PostType postType, Long postId);
}
