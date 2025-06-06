package com.project.stationery_be_server.listener;

import com.project.stationery_be_server.entity.PurchaseOrder;
import com.project.stationery_be_server.service.NotificationService;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;

public class PurchaseOrderListener {
    @Autowired
    private NotificationService notificationService;

    @PostUpdate
    public void onStatusUpdate(PurchaseOrder purchaseOrder) {
        notificationService.notifyOrderUpdate(purchaseOrder, "Đơn hàng của bạn đã được cập nhật trạng thái thành: " + purchaseOrder.getStatus());
    }
}
