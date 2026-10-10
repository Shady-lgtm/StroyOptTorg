package com.example.stroyopttorg.model;

import com.example.stroyopttorg.util.SlugUtil;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_category", columnList = "category_id"),
        @Index(name = "idx_product_brand", columnList = "brand_id")
})
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    /** Артикул */
    @Column(unique = true)
    private String sku;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    /** Eski narx (chegirma ko'rsatish uchun) */
    @Column(precision = 12, scale = 2)
    private BigDecimal oldPrice;

    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "url", length = 1000)
    @OrderColumn(name = "sort_index")
    @BatchSize(size = 50)
    private List<String> images = new ArrayList<>();

    /** Характеристики: "Мощность" -> "750 Вт" */
    @ElementCollection
    @CollectionTable(name = "product_specifications", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "spec_name")
    @Column(name = "spec_value", length = 1000)
    @BatchSize(size = 50)
    private Map<String, String> specifications = new LinkedHashMap<>();

    private boolean newArrival;
    private boolean hit;
    private boolean active = true;

    private double rating;
    private int reviewsCount;

    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        ensureSlug();
    }

    @PreUpdate
    void ensureSlug() {
        if (slug == null || slug.isBlank()) slug = SlugUtil.slugify(name);
    }
}
