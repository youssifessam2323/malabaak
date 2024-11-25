package io.joework.malabaakapi.mapper;

import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.dto.SignupRequest;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
        )
public interface UserMapper {
        User fromSignupRequest(SignupRequest signupRequest);
}
