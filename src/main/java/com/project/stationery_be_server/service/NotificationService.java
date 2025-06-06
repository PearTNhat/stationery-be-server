package com.project.stationery_be_server.service;

import com.project.stationery_be_server.entity.Notification;
import com.project.stationery_be_server.entity.PurchaseOrder;
import com.project.stationery_be_server.entity.UserPromotion;

import java.util.List;

public interface NotificationService {
    void notifyOrderUpdate(PurchaseOrder order, String message);
    void notifyPromotion(UserPromotion userPromotion, String message);
    List<Notification> getUserNotifications(String userId);
    void markAsRead(String notificationId);
}
