package com.example.stroyopttorg.dto;

import java.math.BigDecimal;

public record CartItemDto(Long productId, String name, String slug, String sku, String image, BigDecimal unitPrice,
                          int quantity, BigDecimal lineTotal, int stock) {
}
