package com.project.stationery_be_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     String user_id;
     String first_name;
     String last_name;
     String email;
     String phone;
     String address_id; // Foreign key to Address
     Date dob;
}
