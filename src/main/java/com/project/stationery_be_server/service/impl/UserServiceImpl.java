package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.request.RegisterRequest;
import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.mapper.UserMapper;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Mã hóa mật khẩu
                .isBlock(false)
                .build();

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }
     public UserResponse getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();
        User user =userRepository.findById(id).orElseThrow(()-> new AppException(NotExistedErrorCode.USER_NOT_EXISTED));
         System.out.println(user.getAvatar());
        return userMapper.toUserResponse(user);
     }

}
