package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.StaticPage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaticPageRepository extends JpaRepository<StaticPage, Long> {
    Optional<StaticPage> findBySlug(String slug);
}
