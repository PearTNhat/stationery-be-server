package com.project.stationery_be_server.exception;

import com.project.stationery_be_server.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> exceptionHandler(Exception ex, WebRequest request) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message(ex.getMessage())
                .code(400)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(value = RuntimeException.class)
//    ResponseEntity<ApiResponse<Void>> runtimeExceptionHandler(RuntimeException e) {
//        ApiResponse<Void> apiResponse = new ApiResponse<>();
//        apiResponse.setMessage(e.getMessage());
//        apiResponse.setCode(400);
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//    Báo lỗi k tìm thấy đường dẫn
    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponse<Void>> noResourceFoundException(NoResourceFoundException e) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Path does not exist");
        apiResponse.setCode(404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Void>> appExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }
}
