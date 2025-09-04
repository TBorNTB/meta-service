package com.sejong.metaservice.infrastructure.view.kafka;


import com.sejong.metaservice.core.common.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewEvent {
    private Long postId;
    private PostType postType;
    private Long viewCount;

    public static ViewEvent of(PostType postType, Long postId, Long viewCount) {
        return ViewEvent.builder()
                .postType(postType)
                .postId(postId)
                .viewCount(viewCount)
                .build();
    }
}