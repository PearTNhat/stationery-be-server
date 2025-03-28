package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.ForgotPasswordRequest;
import com.project.stationery_be_server.dto.request.LoginRequest;
import com.project.stationery_be_server.dto.request.OtpVerificationRequest;
import com.project.stationery_be_server.dto.request.RegisterRequest;
import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.LoginResponse;
import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.entity.InvalidatedToken;
import com.project.stationery_be_server.repository.AddressRepository;
import com.project.stationery_be_server.repository.InvalidatedTokenRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.AuthenticateService;
import com.project.stationery_be_server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {
    private final AuthenticateService authenticateService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    public AuthenticateController(AuthenticateService authenticateService, AddressRepository addressRepository, UserRepository userRepository, UserService userService, InvalidatedTokenRepository invalidatedTokenRepository) {
        this.authenticateService = authenticateService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .message("Login successfully")
                .result(authenticateService.login(request))
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<String>builder()
                            .message("Authorization header is missing")
                            .build()
            );
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (invalidatedTokenRepository.existsById(token)) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<String>builder()
                            .message("Token already blacklisted")
                            .build()
            );
        }
        InvalidatedToken invalidatedToken = new InvalidatedToken(token, LocalDateTime.now());
        invalidatedTokenRepository.save(invalidatedToken);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .message("Logout successful")
                        .result("Token invalidated successfully")
                        .build()
        );
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
