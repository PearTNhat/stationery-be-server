package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.UserRequest;
import com.project.stationery_be_server.dto.response.UserResponse;

import java.util.List;


public interface UserService {
    List<UserResponse> getAll();
    UserResponse getUserInfo();
    UserResponse updateUser(String userId, UserRequest request);

}
