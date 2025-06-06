package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.entity.Notification;
import com.project.stationery_be_server.entity.PurchaseOrder;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.entity.UserPromotion;
import com.project.stationery_be_server.repository.NotificationRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Triển khai Dịch vụ Thông báo
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void notifyOrderUpdate(PurchaseOrder order, String message) {
        Notification notification = Notification.builder()
                .user(order.getUser())
                .title("Cập nhật đơn hàng")
                .message(message)
                .type(Notification.NotificationType.ORDER_UPDATE)
                .purchaseOrder(order)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void notifyPromotion(UserPromotion userPromotion, String message) {
        Notification notification = Notification.builder()
                .user(userPromotion.getUser())
                .title("Khuyến mãi mới")
                .message(message)
                .type(Notification.NotificationType.PROMOTION)
                .userPromotion(userPromotion)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }
}
