package io.joework.malabaakapi.model.config;

import java.time.temporal.ChronoUnit;

public record ExpirationTime(long expirationTime, ChronoUnit timeUnit) {
}
