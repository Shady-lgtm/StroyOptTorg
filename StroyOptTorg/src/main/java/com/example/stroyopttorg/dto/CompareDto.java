package com.example.stroyopttorg.dto;

import java.util.List;

/** Сравнение: specKeys - jadval qatorlari, products[i].specifications - qiymatlar */
public record CompareDto(List<ProductDto> products, List<String> specKeys) {
}
