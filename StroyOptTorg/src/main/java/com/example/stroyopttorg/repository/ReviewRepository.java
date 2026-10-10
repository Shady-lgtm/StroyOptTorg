package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);

    boolean existsByProductIdAndUserId(Long productId, Long userId);

    long countByProductId(Long productId);

    @Query("select coalesce(avg(r.rating), 0.0) from Review r where r.product.id = :productId")
    Double averageRating(@Param("productId") Long productId);
}
