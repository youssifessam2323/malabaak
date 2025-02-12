package io.joework.malabaakapi.configurations;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessTokenConfig {
    private String expiryTimeInSeconds;
}
