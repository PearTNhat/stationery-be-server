package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.response.PromotionResponse;
import java.util.List;

public interface UserPromotionService {
    List<PromotionResponse> getVouchersForUser(String userId);
}
