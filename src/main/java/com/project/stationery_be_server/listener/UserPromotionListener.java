package com.project.stationery_be_server.listener;

import com.project.stationery_be_server.entity.UserPromotion;
import com.project.stationery_be_server.service.NotificationService;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;

public class UserPromotionListener {
    @Autowired
    private NotificationService notificationService;

    @PostPersist
    public void onPromotionAssigned(UserPromotion userPromotion) {
        notificationService.notifyPromotion(userPromotion, "Bạn đã nhận được một khuyến mãi mới!");
    }
}
