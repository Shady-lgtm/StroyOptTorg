package com.example.stroyopttorg.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** Mahsulot kartochkasi (to'liq) */
public record ProductDto(Long id, String name, String slug, String sku, String description, BigDecimal price,
                         BigDecimal oldPrice, int discountPercent, int stock, boolean inStock, Long categoryId,
                         String categoryName, Long brandId, String brandName, List<String> images,
                         Map<String, String> specifications, double rating, int reviewsCount, boolean newArrival,
                         boolean hit, boolean active) {
}
