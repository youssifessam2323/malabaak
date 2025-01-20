package io.joework.malabaakapi.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RSAKeysProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {}
