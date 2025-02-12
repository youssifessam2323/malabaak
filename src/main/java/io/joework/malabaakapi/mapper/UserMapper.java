package io.joework.malabaakapi.mapper;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.dto.SignupRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
        )
public interface UserMapper {
        UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

        User fromSignupRequest(SignupRequest signupRequest);
}
