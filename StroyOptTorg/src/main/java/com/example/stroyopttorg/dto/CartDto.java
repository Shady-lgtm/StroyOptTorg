package com.example.stroyopttorg.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(List<CartItemDto> items, int totalQuantity, BigDecimal total) {
}
