package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.entity.Notification;
import com.project.stationery_be_server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ApiResponse<List<Notification>> getUserNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ApiResponse.<List<Notification>>builder()
                .result(notifications)
                .build();
    }

    @PutMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ApiResponse.<Void>builder()
                .build();
    }
}