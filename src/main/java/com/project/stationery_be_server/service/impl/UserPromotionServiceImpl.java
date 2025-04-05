package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.response.PromotionResponse;
import com.project.stationery_be_server.entity.UserPromotion;
import com.project.stationery_be_server.repository.UserPromotionRepository;
import com.project.stationery_be_server.service.UserPromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserPromotionServiceImpl implements UserPromotionService {
    final UserPromotionRepository userPromotionRepository;

    @Override
    public List<PromotionResponse> getVouchersForUser() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        List<UserPromotion> userPromotions = userPromotionRepository.findByUserUserId(userId);

        return userPromotions.stream().map(up -> {
            var promo = up.getPromotion();
            return PromotionResponse.builder()
                    .promotionId(promo.getPromotionId())
                    .promoCode(promo.getPromoCode())
                    .discountType(promo.getDiscountType().name())
                    .discountValue(promo.getDiscountValue())
                    .maxValue(promo.getMaxValue())
                    .minOrderValue(promo.getMinOrderValue())
                    .startDate(promo.getStartDate())
                    .endDate(promo.getEndDate())
                    .build();
        }).collect(Collectors.toList());
    }
}
