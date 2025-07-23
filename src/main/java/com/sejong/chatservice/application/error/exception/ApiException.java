package com.sejong.chatservice.application.error.exception;


import com.sejong.chatservice.application.error.code.ErrorCodeIfs;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCodeIfs errorCodeIfs;
    private final String errorDescription;

    public ApiException(ErrorCodeIfs errorCodeIfs){
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs=errorCodeIfs;
        errorDescription=errorCodeIfs.getDescription();
    }

    public ApiException(ErrorCodeIfs errorCodeIfs, String errorDescription){
        super(errorDescription);
        this.errorCodeIfs=errorCodeIfs;
        this.errorDescription=errorDescription;

    }

    public ApiException(ErrorCodeIfs errorCodeIfs, Throwable tx){
        super(tx);
        this.errorCodeIfs=errorCodeIfs;
        this.errorDescription=errorCodeIfs.getDescription();
    }

    public ApiException(ErrorCodeIfs errorCodeIfs , Throwable tx, String errorDescription){
        super(tx);
        this.errorCodeIfs=errorCodeIfs;
        this.errorDescription=errorDescription;
    }


}
