package io.joework.malabaakapi.mapper;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.dto.SignupRequest;
import io.joework.malabaakapi.model.dto.UserDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
        )
public interface UserMapper {
        UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
        User fromSignupRequest(SignupRequest signupRequest);
        UserDto toUserDto(User user);


}
