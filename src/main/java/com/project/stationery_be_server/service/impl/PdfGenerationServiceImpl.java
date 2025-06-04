package com.project.stationery_be_server.service.impl;

import com.cloudinary.Cloudinary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.entity.PurchaseOrder;
import com.project.stationery_be_server.entity.PurchaseOrderDetail;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.repository.PurchaseOrderRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.PdfGenerationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfGenerationServiceImpl implements PdfGenerationService {
    private static final Logger logger = LoggerFactory.getLogger(PdfGenerationServiceImpl.class);
    private final Cloudinary cloudinary;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    @Override
    public String generateAndUploadInvoicePdf(PurchaseOrder purchaseOrder) {
        try {
            logger.info("Generating invoice PDF for orderId: {}", purchaseOrder.getPurchaseOrderId());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Invoice")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold());
            document.add(new Paragraph("Order ID: " + purchaseOrder.getPurchaseOrderId()));
            document.add(new Paragraph("Date: " + LocalDate.now()));
            document.add(new Paragraph("Customer: " + purchaseOrder.getUser().getFirstName() + " " + purchaseOrder.getUser().getLastName()));
            document.add(new Paragraph("Address: " + purchaseOrder.getAddress().getAddressName()));
            document.add(new Paragraph("\n"));

            float[] columnWidths = {200, 100, 100, 100};
            Table table = new Table(columnWidths);
            table.addHeaderCell("Product");
            table.addHeaderCell("Quantity");
            table.addHeaderCell("Unit Price");
            table.addHeaderCell("Total");

            List<PurchaseOrderDetail> details = purchaseOrder.getPurchaseOrderDetails();
            if (details.isEmpty()) {
                logger.warn("No purchase order details found for orderId: {}", purchaseOrder.getPurchaseOrderId());
                throw new AppException(NotExistedErrorCode.ORDER_NOT_FOUND);
            }

            for (PurchaseOrderDetail detail : details) {
                table.addCell(detail.getProductDetail().getName());
                table.addCell(String.valueOf(detail.getQuantity()));
                table.addCell(String.valueOf(detail.getDiscountPrice()));
                table.addCell(String.valueOf(detail.getDiscountPrice() * detail.getQuantity()));
            }

            table.addCell("");
            table.addCell("");
            table.addCell("Total Amount:");
            table.addCell(String.valueOf(purchaseOrder.getAmount()));

            document.add(table);
            document.close();

            // Lưu PDF cục bộ để kiểm tra
            String localPath = "monthly_invoice_" + purchaseOrder.getPurchaseOrderId() + ".pdf";
            Files.write(Paths.get(localPath), baos.toByteArray());
            logger.info("Saved invoice PDF locally at: {}", localPath);

            Map uploadResult = cloudinary.uploader().upload(baos.toByteArray(),
                    Map.of("resource_type", "raw", "public_id", "invoices/" + purchaseOrder.getPurchaseOrderId() + ".pdf"));
            String url = (String) uploadResult.get("secure_url");
            logger.info("Uploaded invoice PDF to Cloudinary: {}", url);

            return url;
        } catch (Exception e) {
            logger.error("Failed to generate or upload invoice PDF for orderId: {}", purchaseOrder.getPurchaseOrderId(), e);
            throw new RuntimeException("Failed to generate or upload invoice PDF: " + e.getMessage());
        }
    }

    @Override
    public String generateAndUploadMonthlyInvoicePdf(String userId, int month, int year) {
        try {
            logger.info("Generating monthly invoice PDF for userId: {}, month: {}, year: {}", userId, month, year);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(NotExistedErrorCode.USER_NOT_EXISTED));
            if (!user.getRole().getRoleId().equals("113")) {
                throw new AppException(NotExistedErrorCode.USER_EXISTED);
            }

            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
            List<PurchaseOrder> orders = purchaseOrderRepository.findByUser_UserIdAndCreatedAtBetween(
                    userId, startOfMonth, endOfMonth
            );

            if (orders.isEmpty()) {
                logger.warn("No orders found for userId: {}, month: {}, year: {}", userId, month, year);
                throw new AppException(NotExistedErrorCode.ORDER_NOT_FOUND);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Monthly Invoice")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold());
            document.add(new Paragraph("Department: " + user.getFirstName() + " " + user.getLastName()));
            document.add(new Paragraph("Month: " + month + "/" + year));
            document.add(new Paragraph("Date: " + LocalDate.now()));
            document.add(new Paragraph("\n"));

            float[] columnWidths = {100, 200, 100, 100, 100};
            Table table = new Table(columnWidths);
            table.addHeaderCell("Order ID");
            table.addHeaderCell("Product");
            table.addHeaderCell("Quantity");
            table.addHeaderCell("Unit Price");
            table.addHeaderCell("Total");

            long totalAmount = 0;
            boolean hasDetails = false;
            for (PurchaseOrder order : orders) {
                List<PurchaseOrderDetail> details = order.getPurchaseOrderDetails();
                if (details.isEmpty()) {
                    logger.warn("No purchase order details found for orderId: {}", order.getPurchaseOrderId());
                    continue;
                }
                for (PurchaseOrderDetail detail : details) {
                    table.addCell(order.getPurchaseOrderId());
                    table.addCell(detail.getProductDetail().getName());
                    table.addCell(String.valueOf(detail.getQuantity()));
                    table.addCell(String.valueOf(detail.getDiscountPrice()));
                    table.addCell(String.valueOf(detail.getDiscountPrice() * detail.getQuantity()));
                    totalAmount += detail.getDiscountPrice() * detail.getQuantity();
                    hasDetails = true;
                }
            }

            if (!hasDetails) {
                logger.warn("No valid purchase order details found for userId: {}, month: {}, year: {}", userId, month, year);
                throw new AppException(NotExistedErrorCode.ORDER_NOT_FOUND);
            }

            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("Total Amount:");
            table.addCell(String.valueOf(totalAmount));

            document.add(table);
            document.close();

            // Lưu PDF cục bộ để kiểm tra
            String localPath = "monthly_invoice_" + userId + "_" + month + "_" + year + ".pdf";
            Files.write(Paths.get(localPath), baos.toByteArray());
            logger.info("Saved monthly invoice PDF locally at: {}", localPath);

            String publicId = "monthly_invoices/" + userId + "_" + month + "_" + year + ".pdf";
            Map uploadResult = cloudinary.uploader().upload(baos.toByteArray(),
                    Map.of("resource_type", "raw", "public_id", publicId));
            String url = (String) uploadResult.get("secure_url");
            logger.info("Uploaded monthly invoice PDF to Cloudinary: {}", url);

            return url;
        } catch (Exception e) {
            logger.error("Failed to generate or upload monthly invoice PDF for userId: {}, month: {}, year: {}", userId, month, year, e);
            throw new RuntimeException("Failed to generate or upload monthly invoice PDF: " + e.getMessage());
        }
    }
}