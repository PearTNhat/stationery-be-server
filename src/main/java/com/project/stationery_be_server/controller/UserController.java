package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.EmailRequest;
import com.project.stationery_be_server.dto.request.ForgotPasswordRequest;
import com.project.stationery_be_server.dto.request.OtpVerificationRequest;
import com.project.stationery_be_server.dto.request.RegisterRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.service.UploadImageFile;
import com.project.stationery_be_server.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    UploadImageFile uploadImageFile;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .build();
    }

    @PostMapping("/upload") // Sửa @RequestMapping thành @PostMapping cho ngắn gọn
    public ApiResponse<Map> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.<Map>builder()
                .result(uploadImageFile.uploadImageFile(file))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<String> registerUser(@RequestBody RegisterRequest request) {
        String message = userService.register(request);
        return ApiResponse.<String>builder()
                .message("Registration initiated, please check your email for OTP")
                .result(message)
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<UserResponse> verifyOtp(@RequestBody OtpVerificationRequest request) {
        UserResponse userResponse = userService.verifyOtp(request);
        return ApiResponse.<UserResponse>builder()
                .message("User registered successfully")
                .result(userResponse)
                .build();
    }

    @PostMapping("/resend-otp")
    public ApiResponse<String> resendOtp(@RequestBody OtpVerificationRequest request) {
        String message = userService.resendOtp(request.getEmail());
        return ApiResponse.<String>builder()
                .message("OTP sent successfully")
                .result(message)
                .build();
    }

}