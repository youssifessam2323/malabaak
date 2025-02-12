package io.joework.malabaakapi.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "token")
public class TokensConfigProperties {
    private AccessTokenConfig accessTokenConfig;

    private RefreshTokenConfig refreshTokenConfig;
}
