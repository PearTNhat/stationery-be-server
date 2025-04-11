package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.dto.request.AddressRequest;
import com.project.stationery_be_server.dto.response.AddressResponse;
import com.project.stationery_be_server.dto.response.ColorResponse;
import com.project.stationery_be_server.entity.Address;
import com.project.stationery_be_server.entity.Color;
import com.project.stationery_be_server.entity.User;
import com.project.stationery_be_server.repository.AddressRepository;
import com.project.stationery_be_server.repository.UserRepository;
import com.project.stationery_be_server.service.AddressService;
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
public class AddressServiceImpl implements AddressService {
    final AddressRepository addressRepository;
    final UserRepository userRepository;

    @Override
    public AddressResponse createAddress(AddressRequest addressRequest) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        Address address = new Address();
        address.setAddressName(addressRequest.getAddressName());
        address.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        Address savedAddress = addressRepository.save(address);

        return new AddressResponse(savedAddress.getAddressId(), savedAddress.getAddressName(), savedAddress.getUser());
    }

    @Override
    public List<AddressResponse> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> new AddressResponse(address.getAddressId(), address.getAddressName(), address.getUser()))
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse updateAddress(String id, AddressRequest addressRequest) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));

        address.setAddressName(addressRequest.getAddressName());
        address.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));

        Address updatedAddress = addressRepository.save(address);
        return new AddressResponse(updatedAddress.getAddressId(), updatedAddress.getAddressName(), updatedAddress.getUser());
    }
    @Override
    public void deleteAddress(String id) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName(); // Lấy ID người dùng hiện tại

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));

        // Kiểm tra xem địa chỉ có thuộc về người dùng hiện tại không
        if (!address.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this address");
        }

        addressRepository.delete(address);
    }


}
