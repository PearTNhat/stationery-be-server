package com.project.stationery_be_server.controller;

import com.project.stationery_be_server.dto.response.ApiResponse;
import com.project.stationery_be_server.dto.response.MonthlyInvoiceSummaryResponse;
import com.project.stationery_be_server.dto.response.momo.MomoResponse;
import com.project.stationery_be_server.service.DepartmentInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department-invoices")
@RequiredArgsConstructor
public class DepartmentInvoiceController {
    private final DepartmentInvoiceService departmentInvoiceService;

    @GetMapping("/monthly-summary")
    public ApiResponse<MonthlyInvoiceSummaryResponse> getMonthlyInvoiceSummary(
            @RequestParam int month,
            @RequestParam int year) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        MonthlyInvoiceSummaryResponse summary = departmentInvoiceService.getMonthlyInvoiceSummary(userId, month, year);
        String message = summary.getOrderCount() == 0
                ? "No orders found for the specified month"
                : "Monthly invoice summary retrieved successfully";

        return ApiResponse.<MonthlyInvoiceSummaryResponse>builder()
                .code(200)
                .message(message)
                .result(summary)
                .build();
    }

    @PostMapping("/generate-monthly-invoice")
    public ApiResponse<String> generateMonthlyInvoice(
            @RequestParam int month,
            @RequestParam int year) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String pdfUrl = departmentInvoiceService.generateMonthlyInvoicePdf(userId, month, year);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Monthly invoice PDF generated and uploaded successfully")
                .result(pdfUrl)
                .build();
    }

    @PostMapping("/pay-monthly-invoice")
    public ApiResponse<MomoResponse> payMonthlyInvoice(
            @RequestParam int month,
            @RequestParam int year) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        MomoResponse response = departmentInvoiceService.payMonthlyInvoice(userId, month, year);
        return ApiResponse.<MomoResponse>builder()
                .code(200)
                .message("Monthly invoice payment initiated successfully")
                .result(response)
                .build();
    }

    @GetMapping("/check-overdue")
    public ApiResponse<List<String>> checkOverdueInvoices() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> notifications = departmentInvoiceService.checkOverdueInvoices(userId);
        String message = notifications.isEmpty()
                ? "No overdue invoices found"
                : "Overdue invoices retrieved successfully";

        return ApiResponse.<List<String>>builder()
                .code(200)
                .message(message)
                .result(notifications)
                .build();
    }
}