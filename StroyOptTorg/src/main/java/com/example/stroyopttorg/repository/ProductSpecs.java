package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.dto.ProductFilter;
import com.example.stroyopttorg.model.Product;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecs {
    private ProductSpecs() {
    }

    public static Specification<Product> filter(ProductFilter f, Set<Long> categoryIds, boolean includeInactive) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            if (!includeInactive) {
                ps.add(cb.isTrue(root.<Boolean>get("active")));
            }
            if (categoryIds != null) {
                ps.add(root.get("category").get("id").in(categoryIds));
            }
            if (f.brandIds() != null && !f.brandIds().isEmpty()) {
                ps.add(root.get("brand").get("id").in(f.brandIds()));
            }
            if (f.minPrice() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("price"), f.minPrice()));
            }
            if (f.maxPrice() != null) {
                ps.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("price"), f.maxPrice()));
            }
            if (Boolean.TRUE.equals(f.inStock())) {
                ps.add(cb.greaterThan(root.<Integer>get("stock"), 0));
            }
            if (Boolean.TRUE.equals(f.onSale())) {
                ps.add(cb.isNotNull(root.get("oldPrice")));
            }
            if (f.q() != null && !f.q().isBlank()) {
                String like = "%" + f.q().trim().toLowerCase() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(root.<String>get("name")), like),
                        cb.like(cb.lower(root.<String>get("sku")), like)));
            }
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }
}
