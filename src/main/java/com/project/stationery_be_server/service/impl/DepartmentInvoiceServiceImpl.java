package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.Error.NotExistedErrorCode;
import com.project.stationery_be_server.dto.request.MomoRequest;
import com.project.stationery_be_server.dto.response.momo.MomoResponse;
import com.project.stationery_be_server.dto.response.MonthlyInvoiceSummaryResponse;
import com.project.stationery_be_server.entity.PurchaseOrder;
import com.project.stationery_be_server.entity.PurchaseOrderDetail;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.exception.AppException;
import com.project.stationery_be_server.repository.PurchaseOrderDetailRepository;
import com.project.stationery_be_server.repository.PurchaseOrderRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.repository.PromotionRepository;
import com.project.stationery_be_server.repository.ProductDetailRepository;
import com.project.stationery_be_server.repository.PaymentRepository;
import com.project.stationery_be_server.service.PdfGenerationService;
import com.project.stationery_be_server.service.DepartmentInvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.hc.client5.http.utils.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentInvoiceServiceImpl implements DepartmentInvoiceService {
    WebClient webClient;
    PurchaseOrderRepository purchaseOrderRepository;
    UserRepository userRepository;
    PdfGenerationService pdfGenerationService;
    PaymentRepository paymentRepository;
    ProductDetailRepository productDetailRepository;
    PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    PromotionRepository promotionRepository;

    @Value(value = "${momo.partnerCode}")
    @NonFinal
    String partnerCode;
    @Value(value = "${momo.accessKey}")
    @NonFinal
    String accessKey;
    @Value(value = "${momo.secretKey}")
    @NonFinal
    String secretKey;
    @Value(value = "${momo.redirectUrl}")
    @NonFinal
    String redirectUrl;
    @Value(value = "${momo.ipnUrl}")
    @NonFinal
    String ipnUrl;
    @Value(value = "${momo.requestType}")
    @NonFinal
    String requestType;
    @Value(value = "${momo.endpoint}")
    @NonFinal
    String endpoint;

    @Transactional(readOnly = true)
    @Override
    public MonthlyInvoiceSummaryResponse getCurrentMonthInvoiceSummary(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(NotExistedErrorCode.USER_NOT_EXISTED));
        if (!user.getRole().getRoleId().equals("113")) {
            throw new AppException(NotExistedErrorCode.USER_EXISTED);
        }

        // Find the last invoice to determine the start date
        PurchaseOrder lastInvoice = purchaseOrderRepository.findTopByUser_UserIdAndNoteContainingOrderByCreatedAtDesc(
                userId, "Monthly invoice"
        );

        LocalDateTime startDate = lastInvoice != null ? lastInvoice.getCreatedAt() : LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();

        List<PurchaseOrder> orders = purchaseOrderRepository.findByUser_UserIdAndCreatedAtBetween(
                userId, startDate, endDate
        );

        BigDecimal totalAmount = orders.stream()
                .filter(order -> order.getStatus() == PurchaseOrder.Status.COMPLETED)
                .map(order -> BigDecimal.valueOf(order.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return MonthlyInvoiceSummaryResponse.builder()
                .userId(userId)
                .month(LocalDateTime.now().getMonthValue())
                .year(LocalDateTime.now().getYear())
                .totalAmount(totalAmount)
                .orderCount(orders.size())
                .build();
    }

    @Transactional
    @Override
    public String generateCurrentInvoicePdf(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(NotExistedErrorCode.USER_NOT_EXISTED));
        if (!user.getRole().getRoleId().equals("113")) {
            throw new AppException(NotExistedErrorCode.USER_EXISTED);
        }

        // Find the last invoice to determine the start date
        PurchaseOrder lastInvoice = purchaseOrderRepository.findTopByUser_UserIdAndNoteContainingOrderByCreatedAtDesc(
                userId, "Monthly invoice"
        );

        LocalDateTime startDate = lastInvoice != null ? lastInvoice.getCreatedAt() : LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();

        String pdfUrl = pdfGenerationService.generateAndUploadCurrentInvoicePdf(userId, startDate, endDate);

        PurchaseOrder summaryOrder = PurchaseOrder.builder()
                .purchaseOrderId(UUID.randomUUID().toString().replace("-", "").toUpperCase())
                .user(user)
                .status(PurchaseOrder.Status.COMPLETED)
                .amount(getCurrentMonthInvoiceSummary(userId).getTotalAmount().longValue())
                .createdAt(LocalDateTime.now())
                .note("Monthly invoice from " + startDate.toLocalDate() + " to " + endDate.toLocalDate())
                .pdfUrl(pdfUrl)
                .purchaseOrderDetails(new ArrayList<>())
                .build();

        purchaseOrderRepository.save(summaryOrder);
        return pdfUrl;
    }

    @Transactional
    @Override
    public MomoResponse payCurrentInvoice(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(NotExistedErrorCode.USER_NOT_EXISTED));
        if (!user.getRole().getRoleId().equals("113")) {
            throw new AppException(NotExistedErrorCode.USER_EXISTED);
        }

        // Find the last invoice to determine the start date
        PurchaseOrder lastInvoice = purchaseOrderRepository.findTopByUser_UserIdAndNoteContainingOrderByCreatedAtDesc(
                userId, "Monthly invoice"
        );

        LocalDateTime startDate = lastInvoice != null ? lastInvoice.getCreatedAt() : LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now();

        List<PurchaseOrder> orders = purchaseOrderRepository.findByUser_UserIdAndCreatedAtBetween(
                userId, startDate, endDate
        );

        if (orders.isEmpty()) {
            throw new AppException(NotExistedErrorCode.ORDER_NOT_FOUND);
        }

        long totalAmount = 0;
        boolean hasDetails = false;
        for (PurchaseOrder order : orders) {
            if (order.getStatus() != PurchaseOrder.Status.COMPLETED) {
                continue;
            }
            List<PurchaseOrderDetail> details = order.getPurchaseOrderDetails();
            if (details.isEmpty()) {
                continue;
            }
            for (PurchaseOrderDetail detail : details) {
                totalAmount += detail.getDiscountPrice() * detail.getQuantity();
                hasDetails = true;
            }
        }

        if (!hasDetails || totalAmount <= 0) {
            throw new AppException(NotExistedErrorCode.ORDER_NOT_FOUND);
        }

        String orderId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        PurchaseOrder monthlyOrder = PurchaseOrder.builder()
                .purchaseOrderId(orderId)
                .user(user)
                .status(PurchaseOrder.Status.COMPLETED)
                .amount(totalAmount)
                .createdAt(LocalDateTime.now())
                .note("Monthly payment from " + startDate.toLocalDate() + " to " + endDate.toLocalDate())
                .purchaseOrderDetails(new ArrayList<>())
                .build();

        purchaseOrderRepository.save(monthlyOrder);

        String orderInfo = "Monthly payment from " + startDate.toLocalDate() + " to " + endDate.toLocalDate() + " - Department: " + userId;
        String requestId = UUID.randomUUID().toString();
        String extraData = "monthly_payment";
        String rawSignature = "accessKey=" + accessKey + "&amount=" + totalAmount + "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode + "&redirectUrl=" + redirectUrl + "&requestId=" + requestId +
                "&requestType=" + requestType;

        String signature;
        try {
            signature = generateHmacSHA256(rawSignature, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signature: " + e.getMessage());
        }

        MomoRequest requestMomo = MomoRequest.builder()
                .partnerCode(partnerCode)
                .requestType(requestType)
                .redirectUrl(redirectUrl)
                .orderId(orderId)
                .orderInfo(orderInfo)
                .requestId(requestId)
                .amount(totalAmount)
                .extraData(extraData)
                .ipnUrl(ipnUrl)
                .signature(signature)
                .lang("vi")
                .build();

        MomoResponse response = webClient
                .post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestMomo)
                .retrieve()
                .bodyToMono(MomoResponse.class)
                .block();

        monthlyOrder.setNote(monthlyOrder.getNote() + " | MoMo Request ID: " + requestId);
        purchaseOrderRepository.save(monthlyOrder);

        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> checkOverdueInvoices(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(NotExistedErrorCode.USER_NOT_EXISTED));
        if (!user.getRole().getRoleId().equals("113")) {
            throw new AppException(NotExistedErrorCode.USER_EXISTED);
        }

        LocalDateTime overdueThreshold = LocalDateTime.now().minusDays(30);
        List<PurchaseOrder> overdueOrders = purchaseOrderRepository.findByUser_UserIdAndNoteContainingAndStatus(
                userId, "Monthly invoice", PurchaseOrder.Status.COMPLETED
        );

        List<String> notifications = new ArrayList<>();
        for (PurchaseOrder order : overdueOrders) {
            if (order.getCreatedAt().isBefore(overdueThreshold)) {
                String message = String.format(
                        "Hóa đơn từ %s đến %s (ID: %s) đã quá hạn thanh toán. Vui lòng thanh toán sớm.",
                        order.getNote().split(" ")[2],
                        order.getNote().split(" ")[4],
                        order.getPurchaseOrderId()
                );
                notifications.add(message);
                // TODO: Gửi thông báo qua email hoặc hệ thống thông báo
                // Ví dụ: emailService.sendNotification(user.getEmail(), message);
            }
        }

        return notifications;
    }

    private String generateHmacSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }
}