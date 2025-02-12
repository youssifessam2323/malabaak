package io.joework.malabaakapi.configurations;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenConfig {
    private String expiryTimeInSeconds;
}
