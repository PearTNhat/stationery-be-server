package com.project.stationery_be_server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderRequest {
    private String pdfUrl;
    private String productPromotionId;
    private String userPromotionId;
    private String orderId;
}