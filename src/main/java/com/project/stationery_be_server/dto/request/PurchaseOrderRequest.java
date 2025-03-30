package com.project.stationery_be_server.dto.request;

import com.project.stationery_be_server.entity.PurchaseOrder;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderRequest {
    private String userId;
    private String pdfUrl;
    private String productPromotionId;
    private String userPromotionId;
}