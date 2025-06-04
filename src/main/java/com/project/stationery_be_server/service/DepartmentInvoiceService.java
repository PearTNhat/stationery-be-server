package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.response.momo.MomoResponse;
import com.project.stationery_be_server.dto.response.MonthlyInvoiceSummaryResponse;

import java.util.List;

public interface DepartmentInvoiceService {
    MonthlyInvoiceSummaryResponse getMonthlyInvoiceSummary(String userId, int month, int year);
    String generateMonthlyInvoicePdf(String userId, int month, int year);
    MomoResponse payMonthlyInvoice(String userId, int month, int year);
    List<String> checkOverdueInvoices(String userId);
}