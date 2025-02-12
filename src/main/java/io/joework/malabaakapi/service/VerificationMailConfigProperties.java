package io.joework.malabaakapi.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.verification")
public record VerificationMailConfigProperties(String baseUrl) {
}
