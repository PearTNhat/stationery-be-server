package com.project.stationery_be_server.Error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements BaseErrorCode {
    UNAUTHENTICATED(1002, "Username or password are incorrect", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1003, "You do not have permission", HttpStatus.FORBIDDEN),
    MISSING_TOKEN(1004, "Missing token or token invalid", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1005, "Invalid token", HttpStatus.UNAUTHORIZED),
    BLOCKED(1006,"Your account has been blocked", HttpStatus.BAD_REQUEST);
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    AuthErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
