package com.example.stroyopttorg.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductRequest(@NotBlank String name, String slug, String sku, String description,
                             @NotNull @DecimalMin("0.0") BigDecimal price, BigDecimal oldPrice,
                             @Min(0) Integer stock, Long categoryId, Long brandId, List<String> images,
                             Map<String, String> specifications, Boolean newArrival, Boolean hit, Boolean active) {
}
