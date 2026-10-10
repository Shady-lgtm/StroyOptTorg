package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.ProductCardDto;
import com.example.stroyopttorg.dto.ProductDto;
import com.example.stroyopttorg.model.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/** Faqat @Transactional kontekstida chaqiriladi (lazy maydonlar bor). */
public final class ProductMapper {
    private ProductMapper() {
    }

    public static int discount(Product p) {
        BigDecimal old = p.getOldPrice();
        if (old == null || old.signum() <= 0 || old.compareTo(p.getPrice()) <= 0) {
            return 0;
        }
        return old.subtract(p.getPrice()).multiply(BigDecimal.valueOf(100))
                .divide(old, 0, RoundingMode.HALF_UP).intValue();
    }

    public static String mainImage(Product p) {
        return p.getImages().isEmpty() ? null : p.getImages().get(0);
    }

    public static ProductCardDto toCard(Product p) {
        return new ProductCardDto(p.getId(), p.getName(), p.getSlug(), p.getSku(), p.getPrice(), p.getOldPrice(),
                discount(p), p.getStock() > 0, mainImage(p), p.getBrand() == null ? null : p.getBrand().getName(),
                p.getRating(), p.getReviewsCount(), p.isNewArrival(), p.isHit(), p.isActive());
    }

    public static ProductDto toDto(Product p) {
        return new ProductDto(p.getId(), p.getName(), p.getSlug(), p.getSku(), p.getDescription(), p.getPrice(),
                p.getOldPrice(), discount(p), p.getStock(), p.getStock() > 0,
                p.getCategory() == null ? null : p.getCategory().getId(),
                p.getCategory() == null ? null : p.getCategory().getName(),
                p.getBrand() == null ? null : p.getBrand().getId(),
                p.getBrand() == null ? null : p.getBrand().getName(),
                new ArrayList<>(p.getImages()), new LinkedHashMap<>(p.getSpecifications()),
                p.getRating(), p.getReviewsCount(), p.isNewArrival(), p.isHit(), p.isActive());
    }
}
