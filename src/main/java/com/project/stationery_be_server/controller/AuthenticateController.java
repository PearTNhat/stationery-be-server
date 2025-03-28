package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.ForgotPasswordRequest;
import com.project.stationery_be_server.dto.request.LoginRequest;
import com.project.stationery_be_server.dto.request.OtpVerificationRequest;
import com.project.stationery_be_server.dto.request.RegisterRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.LoginResponse;
import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.repository.AddressRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.AuthenticateService;
import com.project.stationery_be_server.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {
    private final AuthenticateService authenticateService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthenticateController(AuthenticateService authenticateService, AddressRepository addressRepository, UserRepository userRepository, UserService userService) {
        this.authenticateService = authenticateService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .message("Login successfully")
                .result(authenticateService.login(request))
                .build();
    }
    @GetMapping
    public ApiResponse<?> getUserInfo() {
        return ApiResponse.builder().result(
                userRepository.findByEmail("letuannhat105@gmail.com").get().getRole()
        ).build();
    }
    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String message = userService.forgotPassword(request);
        return ApiResponse.<String>builder()
                .message("Password reset initiated, please check your email for OTP")
                .result(message)
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<UserResponse> resetPassword(
            @RequestBody OtpVerificationRequest otpRequest,
            @RequestParam String newPassword) {
        UserResponse userResponse = userService.resetPassword(otpRequest, newPassword);
        return ApiResponse.<UserResponse>builder()
                .message("Password reset successfully")
                .result(userResponse)
                .build();
    }
}
