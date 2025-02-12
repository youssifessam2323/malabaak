package io.joework.malabaakapi.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthTokens {
    private String accessToken;
    private String refreshToken;
}
