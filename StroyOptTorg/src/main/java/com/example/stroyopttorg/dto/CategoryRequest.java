package com.example.stroyopttorg.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(@NotBlank String name, String slug, String imageUrl, Integer sortOrder, Long parentId) {
}
