package com.example.stroyopttorg.dto;

import java.math.BigDecimal;

/** Katalog ro'yxati / karusel uchun qisqa ko'rinish */
public record ProductCardDto(Long id, String name, String slug, String sku, BigDecimal price, BigDecimal oldPrice,
                             int discountPercent, boolean inStock, String image, String brand, double rating,
                             int reviewsCount, boolean newArrival, boolean hit, boolean active) {
}
