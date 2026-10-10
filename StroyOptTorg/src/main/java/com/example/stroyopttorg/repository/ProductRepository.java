package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.Product;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Override
    @EntityGraph(attributePaths = {"brand", "category"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    Optional<Product> findByIdAndActiveTrue(Long id);

    Optional<Product> findBySlugAndActiveTrue(String slug);

    /** Buyurtma paytida ombor qoldig'ini xavfsiz kamaytirish uchun qulflash */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    List<Product> findTop8ByActiveTrueAndHitTrueOrderByIdDesc();

    List<Product> findTop8ByActiveTrueAndNewArrivalTrueOrderByCreatedAtDesc();

    List<Product> findTop8ByActiveTrueAndOldPriceIsNotNullOrderByIdDesc();

    List<Product> findTop8ByCategoryIdAndActiveTrueAndIdNotOrderByRatingDesc(Long categoryId, Long id);
}
