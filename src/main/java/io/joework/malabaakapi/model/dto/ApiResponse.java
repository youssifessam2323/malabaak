package io.joework.malabaakapi.model.dto;

import lombok.Builder;

@Builder
public record ApiResponse<T>(int statusCode, String message, T payload) {
}
