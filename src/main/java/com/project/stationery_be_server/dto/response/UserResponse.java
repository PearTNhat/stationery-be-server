package com.project.stationery_be_server.dto.response;

import com.project.stationery_be_server.entity.Address;
import com.project.stationery_be_server.entity.Cart;
import com.project.stationery_be_server.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     String user_id;
     String avatar;
     String first_name;
     String last_name;
     String email;
     String phone;
     Set<Address> addresses; // Foreign key to Address
     Role role;
     Set<Cart> carts;
     Date dob;
}
