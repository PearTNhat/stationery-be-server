package com.project.stationery_be_server.service;

import com.project.stationery_be_server.dto.request.AddressRequest;
import com.project.stationery_be_server.dto.response.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service

public interface AddressService {
    AddressResponse createAddress(AddressRequest addressRequest);
    List<AddressResponse> getAllAddresses();
    AddressResponse updateAddress(String id, AddressRequest addressRequest);
}
