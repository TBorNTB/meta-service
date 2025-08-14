package com.sejong.metaservice.core.common.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExceptionType implements ExceptionTypeIfs {

    OK(200,"성공"),
    BAD_REQUEST(400,"잘못된 요청"),
    SERVER_ERROR(500,"서버 에러"),

    NULL_POINT(500,"Null Pointer"),
    MULTI_REQUEST(405,"하루 한번만 요청 가능합니다"),
    BAD_SORT_REQUEST(400,"정렬 방향은 ASC/DESC 만 가능합니다."),
    EXTERNAL_SERVER_ERROR(500,"잠시 서비스 이용이 불가합니다."),

    NOT_FOUND_POST_TYPE_POST_ID(400, "존재하지 않는 포스트 타입, 포스트 id")
    ;

    private final Integer httpStatus;
    private final String description;

    @Override
    public Integer httpStatus() {
        return httpStatus;
    }

    @Override
    public String description() {
        return description;
    }
}
