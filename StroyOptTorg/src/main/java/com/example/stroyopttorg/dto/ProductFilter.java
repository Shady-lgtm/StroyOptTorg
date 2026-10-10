package com.example.stroyopttorg.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductFilter(String category, List<Long> brandIds, BigDecimal minPrice, BigDecimal maxPrice,
                            String q, Boolean inStock, Boolean onSale) {
}
