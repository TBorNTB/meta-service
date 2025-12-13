package com.sejong.metaservice.infrastructure.kafka;

import com.sejong.metaservice.core.comment.domain.Comment;
import com.sejong.metaservice.core.reply.domain.Reply;
import com.sejong.metaservice.domain.like.domain.Like;
import com.sejong.metaservice.support.common.enums.PostType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static DomainAlarmEvent from(Like like, AlarmType alarmType, String ownerUsername) {
        DomainType makeDomainType = getDomainType(like.getPostType());

        return DomainAlarmEvent.builder()
                .domainId(like.getPostId())
                .alarmType(alarmType)
                .domainType(makeDomainType)
                .actorUsername(like.getUsername())
                .ownerUsername(ownerUsername)
                .createdAt(like.getCreatedAt())
                .build();
    }


    public static DomainAlarmEvent from(Comment comment, AlarmType alarmType, String ownerUsername) {
        DomainType makeDomainType = getDomainType(comment.getPostType());

        return DomainAlarmEvent.builder()
                .domainId(comment.getPostId())
                .alarmType(alarmType)
                .domainType(makeDomainType)
                .actorUsername(comment.getUsername())
                .ownerUsername(ownerUsername)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    /*
    이 친구는 따로 도메인 타입 포스트에 종류에 따라 구분할 필요가 없어서 바로 DomainType.COMMENT 넣었습니다.
       그 이유는  이동호님이 댓글에 응답을 남겼습니다. 와 같이 포스트 종류가 필요 없어집니다.
    */
    public static DomainAlarmEvent from(Comment parentComment, Reply reply, AlarmType alarmType) {
        DomainType makeDomainType = getDomainType(parentComment.getPostType());

        return DomainAlarmEvent.builder()
                .domainId(parentComment.getPostId())
                .alarmType(alarmType)
                .domainType(makeDomainType)
                .actorUsername(reply.getUsername())
                .ownerUsername(parentComment.getUsername())
                .createdAt(reply.getCreatedAt())
                .build();
    }

    private static DomainType getDomainType(PostType postType) {
        DomainType domainType = switch (postType) {
            case NEWS -> DomainType.NEWS;
            case PROJECT -> DomainType.PROJECT;
            case DOCUMENT -> DomainType.DOCUMENT;
            case ARTICLE -> DomainType.ARTICLE;
            default -> DomainType.GLOBAL;
        };
        return domainType;
    }
}
