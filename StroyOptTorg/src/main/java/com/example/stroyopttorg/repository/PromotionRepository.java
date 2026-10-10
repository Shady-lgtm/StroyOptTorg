package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.Promotion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByActiveTrueOrderByIdDesc();

    Optional<Promotion> findBySlugAndActiveTrue(String slug);
}
