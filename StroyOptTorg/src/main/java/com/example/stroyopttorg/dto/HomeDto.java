package com.example.stroyopttorg.dto;

import com.example.stroyopttorg.model.Promotion;
import java.util.List;

public record HomeDto(List<CategoryDto> categories, List<ProductCardDto> hits, List<ProductCardDto> newArrivals,
                      List<ProductCardDto> onSale, List<Promotion> promotions, List<BlogSummaryDto> latestPosts) {
}
