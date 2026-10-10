package com.example.stroyopttorg.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(int status, String message, Map<String, String> fieldErrors, Instant timestamp) {
    public static ApiError of(int status, String message) {
        return new ApiError(status, message, null, Instant.now());
    }
}
