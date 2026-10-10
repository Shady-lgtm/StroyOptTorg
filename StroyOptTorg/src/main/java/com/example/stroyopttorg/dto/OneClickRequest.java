package com.example.stroyopttorg.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Купить в 1 клик */
public record OneClickRequest(String name, @NotBlank String phone, @NotNull Long productId, @Min(1) int quantity) {
}
