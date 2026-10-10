package com.example.stroyopttorg.dto;

import jakarta.validation.constraints.NotBlank;

public record CallbackRequestDto(String name, @NotBlank String phone, String message) {
}
