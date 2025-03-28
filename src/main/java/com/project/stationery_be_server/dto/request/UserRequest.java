package com.project.stationery_be_server.dto.request;

import com.project.stationery_be_server.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String user_id;
    String first_name;
    String last_name;
    String email;
    String phone;
    String password;
    Date dob;
    String avatar;
    boolean isBlock;
    Integer otp;
    String role_id;
}
