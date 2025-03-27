package com.project.stationery_be_server.controller;

import com.cloudinary.Api;
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
    @GetMapping("/info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserInfo())
                .build();
    }
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ApiResponse<Map>uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.<Map>builder()
                .result( uploadImageFile.uploadImageFile(file))
                .build();
    }
    @PostMapping("/register")
    public ApiResponse<UserResponse> registerUser(@RequestBody RegisterRequest request) {
        UserResponse userResponse = userService.register(request);
        return ApiResponse.<UserResponse>builder()
                .message("User registered successfully")
                .result(userResponse)
                .build();
    }
}
