package com.project.stationery_be_server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_EXISTED(1000,"User existed", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    HttpStatus httpStatus;

    ErrorCode(int code, String message,HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus =httpStatus;
    }
}
