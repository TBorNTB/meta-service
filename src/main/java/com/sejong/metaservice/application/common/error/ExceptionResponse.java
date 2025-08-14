package com.sejong.metaservice.application.common.error;

import lombok.Data;

@Data
public class ExceptionResponse {
    private final String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}

