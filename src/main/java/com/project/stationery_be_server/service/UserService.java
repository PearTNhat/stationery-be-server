package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.*;
import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.mapper.UserMapper;
import com.project.stationery_be_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {
    public List<UserResponse> getAll();

    // Phương thức đăng ký tài khoản, Atrả về thông báo gửi OTP
    String register(RegisterRequest request);

    // Phương thức xác nhận OTP, trả về thông tin user sau khi đăng ký thành công
    UserResponse verifyOtp(OtpVerificationRequest otpRequest);

    // Gửi lại OTP nếu người dùng chưa hoàn tất đăng ký
    String resendOtp(String email);

    // New methods for forgot password
    String forgotPassword(ForgotPasswordRequest request);

    UserResponse resetPassword(OtpVerificationRequest otpRequest, String newPassword);

    UserResponse getUserInfo();
    //Change Password
    String changePassword(String email, String oldPassword, String newPassword);

    UserResponse updateUser(String documentJson, MultipartFile file);
}
