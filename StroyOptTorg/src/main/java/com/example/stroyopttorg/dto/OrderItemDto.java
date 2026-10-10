package com.example.stroyopttorg.dto;

import java.math.BigDecimal;

public record OrderItemDto(Long productId, String name, String sku, BigDecimal unitPrice, int quantity,
                           BigDecimal lineTotal) {
}
