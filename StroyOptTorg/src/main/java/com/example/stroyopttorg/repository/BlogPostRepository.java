package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.BlogPost;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Page<BlogPost> findByPublishedTrueOrderByPublishedAtDesc(Pageable pageable);

    Optional<BlogPost> findBySlugAndPublishedTrue(String slug);

    List<BlogPost> findTop3ByPublishedTrueOrderByPublishedAtDesc();
}
