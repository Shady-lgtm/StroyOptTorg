package com.example.stroyopttorg.dto;

import java.util.List;

public record CategoryDto(Long id, String name, String slug, String imageUrl, Long parentId, List<CategoryDto> children) {
}
