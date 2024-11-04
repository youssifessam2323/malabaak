package io.joework.malabaakapi.model.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Builder
public class VerificationLinkConfig {
    private Instant expirationTime;
}
