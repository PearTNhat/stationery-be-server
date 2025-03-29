package com.project.stationery_be_server.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionResponse {
    private String promotionId;
    private String promoCode;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal maxValue;
    private BigDecimal minOrderValue;
    private LocalDate startDate;
    private LocalDate endDate;
}
