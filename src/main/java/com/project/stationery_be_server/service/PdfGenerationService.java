package com.project.stationery_be_server.service;

import com.project.stationery_be_server.entity.PurchaseOrder;

public interface PdfGenerationService {
    String generateAndUploadInvoicePdf(PurchaseOrder purchaseOrder);
    String generateAndUploadMonthlyInvoicePdf(String userId, int month, int year);
}