package com.anubhav.dev.store.mappers;

import com.anubhav.dev.store.dtos.RegisterUserRequest;
import com.anubhav.dev.store.dtos.UpdateUserRequest;
import com.anubhav.dev.store.dtos.UserDto;
import com.anubhav.dev.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);

    void update(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
