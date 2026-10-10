package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.Brand;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findAllByOrderByNameAsc();
}
