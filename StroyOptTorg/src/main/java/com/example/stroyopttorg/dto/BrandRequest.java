package com.example.stroyopttorg.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandRequest(@NotBlank String name, String slug, String logoUrl) {
}
