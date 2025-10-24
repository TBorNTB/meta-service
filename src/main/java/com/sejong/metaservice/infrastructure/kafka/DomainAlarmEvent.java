package com.sejong.metaservice.infrastructure.postlike.kafka;

import com.sejong.metaservice.core.common.enums.PostType;
import com.sejong.metaservice.core.postlike.domain.PostLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomainAlarmEvent {

    private AlarmType alarmType;
    private DomainType domainType;
    private Long domainId;
    private String actorUsername; // 좋아요를 누른 사람을 말합니다.
    private String ownerUsername; // 해당 글의 주인을 의미합니다.
    private LocalDateTime createdAt;

    public static DomainAlarmEvent from(PostLike postLike, AlarmType alarmType, String ownerUsername) {
        DomainType makeDomainType = switch (postLike.getPostType()) {
            case NEWS -> DomainType.NEWS;
            case PROJECT -> DomainType.PROJECT;
            case ARTICLE -> DomainType.ARCHIVE;
            default -> DomainType.GLOBAL;
        };

        return DomainAlarmEvent.builder()
                .domainId(postLike.getPostId())
                .alarmType(alarmType)
                .domainType(makeDomainType)
                .actorUsername(postLike.getUsername())
                .ownerUsername(ownerUsername)
                .createdAt(postLike.getCreatedAt())
                .build();
    }
}
