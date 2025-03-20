package com.project.stationery_be_server.Error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NotExistedErrorCode implements BaseErrorCode {
    USER_NOT_EXISTED(1001, "Username not existed", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    ID_NOT_EXIST(1003, "ID doest not exist", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXIST(1004, "Role doest not exist", HttpStatus.BAD_REQUEST);
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    NotExistedErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
