package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.EmailRequest;

public interface EmailService {
    // Method
    // To send a simple email
    String sendSimpleMail(com.project.stationery_be_server.dto.request.EmailRequest request);
}
