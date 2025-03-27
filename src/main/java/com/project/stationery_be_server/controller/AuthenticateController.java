package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.request.LoginRequest;
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

    public AuthenticateController(AuthenticateService authenticateService, AddressRepository addressRepository, UserRepository userRepository) {
        this.authenticateService = authenticateService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
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
}
