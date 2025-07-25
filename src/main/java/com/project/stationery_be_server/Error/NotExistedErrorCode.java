package com.project.stationery_be_server.Error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NotExistedErrorCode implements BaseErrorCode {
    USER_NOT_EXISTED(1001, "Username not existed", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    ID_NOT_EXIST(1003, "ID doest not exist", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXIST(1004, "Role doest not exist", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1005,"Email already exists", HttpStatus.BAD_REQUEST),
    OTP_NOT_FOUND(1006,"OTP not found", HttpStatus.BAD_REQUEST),
    PENDING_REGISTRATION_EXISTS(1007,"There is a pending login request for email", HttpStatus.BAD_REQUEST),
    PENDING_REGISTRATION_NOT_FOUND(1008,"No pending registration found for email", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_EXITS(1009, "Category name already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1009, "Category not found", HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_EXISTED(1010, "Product does not existed", HttpStatus.BAD_REQUEST),
    PRODUCT_PROMOTION_NOT_EXISTED(1010, "Product promotion does not existed", HttpStatus.BAD_REQUEST),
    PRODUCT_COLOR_NOT_EXISTED(1011, "Product color not existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_ENOUGH(1012, "Product quantity not enough", HttpStatus.BAD_REQUEST),

    // comment
    COMMENT_NOT_FOUND (1022, "Comment not found", HttpStatus.BAD_REQUEST),

    // address
    ADDRESS_NOT_FOUND(1030, "Address not found", HttpStatus.BAD_REQUEST),

    // order
    ORDER_NOT_FOUND(1040, "Order not found", HttpStatus.BAD_REQUEST),
    INVALID_STATUS(1040, "Order status not found", HttpStatus.BAD_REQUEST),
    ORDER_NOT_CANCELLABLE(1041, "Order can't cancel", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EDITABLE(1042, "Order can't edit", HttpStatus.BAD_REQUEST),
    ORDER_STILL_PENDING(1043, "Order still pending", HttpStatus.BAD_REQUEST),
    ORDER_NOT_PENDING(1043, "Order  not pending", HttpStatus.BAD_REQUEST),
    ORDER_NOT_PAY(1040, "Order not found", HttpStatus.BAD_REQUEST),
    //
    IN_ORDER_NOT_FOUND(1043, "In order not found", HttpStatus.BAD_REQUEST),
    PURCHASE_ORDER_NOT_EXISTED(1044, "Purchase order not existed", HttpStatus.BAD_REQUEST),

    // user promotion
    USER_PROMOTION_NOT_FOUND(1050, "User promotion not found", HttpStatus.BAD_REQUEST),

    //payment
    PAYMENT_NOT_FOUND(1060, "Payment not found", HttpStatus.BAD_REQUEST),
    PAYMENT_EXISTS(1061, "Payment already exists", HttpStatus.BAD_REQUEST),

    //category
    CATEGORY_NOT_EXISTED(1070, "Category not existed", HttpStatus.BAD_REQUEST),

    //Color
    SIZE_NOT_EXISTED(1080, "Size not existed", HttpStatus.BAD_REQUEST),
    // size
    COLOR_NOT_EXISTED(1090, "Color not existed", HttpStatus.BAD_REQUEST),

    //role
    ROLE_NOT_EXISTED(1100, "Role not existed", HttpStatus.BAD_REQUEST),
    USER_NOT_ADMIN(1101, "User is not admin", HttpStatus.BAD_REQUEST)
    ;
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    NotExistedErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
