package com.sejong.chatservice.core.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeIfs{

    OK(200,200,"성공"),
    BAD_REQUEST(400,1234,"잘못된 요청"),
    SERVER_ERROR(500,500,"서버 에러"),

    NULL_POINT(500,512,"Null Pointer"),
    MULTI_REQUEST(405,405,"하루 한번만 요청 가능합니다"),
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
