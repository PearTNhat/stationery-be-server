package com.project.stationery_be_server.mapper;


import com.project.stationery_be_server.dto.response.UserResponse;
import com.project.stationery_be_server.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

}
