package io.joework.malabaakapi.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SignupResponse {
    private String email;
    private String fullName;
    private String token;
    private String role;
    private Boolean isVerificationRequired;
}
