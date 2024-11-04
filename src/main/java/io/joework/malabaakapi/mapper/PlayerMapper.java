package io.joework.malabaakapi.mapper;

import io.joework.malabaakapi.model.Player;
import io.joework.malabaakapi.model.dto.SignupRequest;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
        )
public interface PlayerMapper {
        Player fromSignupRequest(SignupRequest signupRequest, @MappingTarget Player player);
}
