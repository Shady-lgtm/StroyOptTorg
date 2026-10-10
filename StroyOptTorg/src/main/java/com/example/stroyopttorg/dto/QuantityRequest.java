package com.example.stroyopttorg.dto;

import jakarta.validation.constraints.Min;

/** quantity = 0 bo'lsa pozitsiya savatdan o'chiriladi */
public record QuantityRequest(@Min(0) int quantity) {
}
