package com.project.stationery_be_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderResponse {
    private String purchaseOrderId;
    private Date createdAt;
    private List<PurchaseOrderDetailResponse> orderDetails;
}