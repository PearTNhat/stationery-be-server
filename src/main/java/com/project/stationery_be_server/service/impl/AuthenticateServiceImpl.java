package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.Error.AuthErrorCode;
import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.request.LoginRequest;
import com.project.stationery_be_server.dto.response.LoginResponse;
import com.project.stationery_be_server.entity.Address;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.mapper.UserMapper;
import com.project.stationery_be_server.repository.AddressRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.AuthenticateService;
import com.project.stationery_be_server.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateServiceImpl implements AuthenticateService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    UserMapper userMapper;
    AddressRepository addressRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
//        System.out.println(passwordEncoder.encode(request.getPassword()));
//         Find user with populated carts
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(NotExistedErrorCode.USER_NOT_EXISTED));
        System.out.println("user: " + user.getRole().getRole_id());
        // Check if user is blocked
        if (user.isBlock()) {
            throw new AppException(AuthErrorCode.BLOCKED);
        }
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(AuthErrorCode.UNAUTHENTICATED);
        }
        // Generate tokens
        String accessToken = jwtUtils.generateToken(user.getUser_id());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .userData(userMapper.toUserResponse(user))
                .build();
    }
}