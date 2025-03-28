package com.project.stationery_be_server.dto.request;
import com.project.stationery_be_server.entity.Address;
import com.project.stationery_be_server.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {
    String address_id;
    String address_name;
    String user_id;
}
