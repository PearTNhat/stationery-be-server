package com.project.stationery_be_server.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    String productId;
    String content;
    String parentId;
    Integer rating;
    String replyOnUser;

}
