package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.Error.AuthErrorCode;
import com.project.stationery_be_server.Error.InvalidErrorCode;
import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.request.EmailRequest;
import com.project.stationery_be_server.dto.request.ForgotPasswordRequest;
import com.project.stationery_be_server.dto.request.OtpVerificationRequest;
import com.project.stationery_be_server.dto.request.RegisterRequest;
import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.mapper.UserMapper;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.EmailService;
import com.project.stationery_be_server.service.UserService;
import com.project.stationery_be_server.utils.OtpUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
    OtpUtils otpUtils;

    // Temporary storage for pending registrations
    private final Map<String, RegisterRequest> pendingRegistrations = new HashMap<>();
    private final Map<String, OtpDetails> otpStorage = new HashMap<>();

    private static class OtpDetails {
        Integer otp;
        Date createdAt;

        OtpDetails(Integer otp, Date createdAt) {
            this.otp = otp;
            this.createdAt = createdAt;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        String email = request.getEmail();
        log.info("Register request received for email: {}", email);

        if (userRepository.existsByEmail(email)) {
            log.warn("Registration failed - email already exists: {}", email);
            throw new AppException(NotExistedErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (pendingRegistrations.containsKey(email)) {
            log.warn("Pending registration already exists for email: {}", email);
            throw new AppException(NotExistedErrorCode.PENDING_REGISTRATION_EXISTS);
        }

        return sendOtp(request);
    }

    @Override
    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail();
        System.out.println("Forgot password request received for email: " + email);
        log.info("Forgot password request received for email: {}", email);

        // Check if user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", email);
                    return new AppException(NotExistedErrorCode.USER_NOT_EXISTED);
                });

        // Generate and store OTP
        Integer otp = otpUtils.generateOTP();
        otpStorage.put(email, new OtpDetails(otp, new Date()));

        log.info("OTP generated for forgot password - email: {}", email);

        try {
            emailService.sendSimpleMail(new EmailRequest(email, otp));
            log.info("Forgot password OTP email sent successfully to: {}", email);
            return "OTP sent to " + email;
        } catch (Exception e) {
            log.error("Failed to send forgot password OTP email to {}: {}", email, e.getMessage());
            otpStorage.remove(email);
            throw new AppException(AuthErrorCode.SEND_MAIL_FAILD);
        }
    }

    @Override
    @Transactional
    public UserResponse resetPassword(OtpVerificationRequest otpRequest, String newPassword) {
        String email = otpRequest.getEmail();
        Integer userOtp = otpRequest.getOtp();

        log.info("Reset password request for email: {}", email);

        // Verify user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", email);
                    return new AppException(NotExistedErrorCode.USER_NOT_EXISTED);
                });

        // Verify OTP
        OtpDetails otpDetails = otpStorage.get(email);
        if (otpDetails == null) {
            log.warn("No OTP found for email: {}", email);
            throw new AppException(NotExistedErrorCode.OTP_NOT_FOUND);
        }

        // Check OTP expiration (5 minutes)
        if (otpDetails.createdAt == null ||
                (new Date().getTime() - otpDetails.createdAt.getTime() > 300_000)) {
            log.info("OTP expired for email: {}", email);
            otpStorage.remove(email);
            throw new AppException(AuthErrorCode.OTP_EXPIRED);
        }

        // Verify OTP match
        if (!otpDetails.otp.equals(userOtp)) {
            log.warn("Invalid OTP for email: {}", email);
            throw new AppException(InvalidErrorCode.INVALID_OTP);
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(otpDetails.otp);  // Update OTP in DB
        user.setOtpCreatedAt(otpDetails.createdAt);

        User updatedUser = userRepository.save(user);

        // Clean up OTP storage
        otpStorage.remove(email);

        log.info("Password reset successfully for email: {}", email);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(NotExistedErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse verifyOtp(OtpVerificationRequest otpRequest) {
        String email = otpRequest.getEmail();
        Integer userOtp = otpRequest.getOtp();

        log.info("Verifying OTP for email: {}", email);

        OtpDetails otpDetails = otpStorage.get(email);
        if (otpDetails == null) {
            log.warn("No OTP found for email: {}", email);
            throw new AppException(NotExistedErrorCode.OTP_NOT_FOUND);
        }

        // Check if OTP is expired (5 minutes = 300,000 milliseconds)
        if (otpDetails.createdAt == null ||
                (new Date().getTime() - otpDetails.createdAt.getTime() > 300_000)) {
            log.info("OTP expired for email: {}", email);
            otpStorage.remove(email);
            pendingRegistrations.remove(email);
            throw new AppException(AuthErrorCode.OTP_EXPIRED);
        }

        // Compare OTPs
        if (!otpDetails.otp.equals(userOtp)) {
            log.warn("Invalid OTP for email: {}", email);
            throw new AppException(InvalidErrorCode.INVALID_OTP);
        }

        RegisterRequest request = pendingRegistrations.get(email);
        if (request == null) {
            log.warn("No pending registration found for email: {}", email);
            throw new AppException(NotExistedErrorCode.PENDING_REGISTRATION_NOT_FOUND);
        }

        // Create user only after successful OTP verification
        User user = User.builder()
                .firstName(request.getFirst_name())
                .lastName(request.getLast_name())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .isBlocked(false)
                .otp(otpDetails.otp)  // Store OTP in DB
                .otpCreatedAt(otpDetails.createdAt)
                .build();

        User savedUser = userRepository.save(user);

        // Clean up temporary storage
        pendingRegistrations.remove(email);
        otpStorage.remove(email);

        log.info("OTP verified and user created successfully for email: {}", email);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public String resendOtp(String email) {
        log.info("Resend OTP requested for email: {}", email);

        RegisterRequest request = pendingRegistrations.get(email);
        if (request == null) {
            throw new AppException(NotExistedErrorCode.PENDING_REGISTRATION_NOT_FOUND);
        }

        return sendOtp(request);
    }

    private String sendOtp(RegisterRequest request) {
        String email = request.getEmail();
        Integer otp = otpUtils.generateOTP();

        // Store registration details and OTP in memory
        pendingRegistrations.put(email, request);
        otpStorage.put(email, new OtpDetails(otp, new Date()));

        log.info("OTP generated for {}: {}", email, otp);

        try {
            emailService.sendSimpleMail(new EmailRequest(email, otp));
            log.info("OTP email sent successfully to: {}", email);
            return "OTP sent to " + email;
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", email, e.getMessage());
            pendingRegistrations.remove(email);
            otpStorage.remove(email);
            throw new AppException(AuthErrorCode.SEND_MAIL_FAILD);
        }
    }
}