package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.mapper.UserMapper;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    UserRepository userRepository;

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
}
