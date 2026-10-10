package com.example.stroyopttorg.model;

import com.example.stroyopttorg.util.SlugUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blog_posts")
@Getter
@Setter
@NoArgsConstructor
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(length = 1000)
    private String excerpt;

    @Column(columnDefinition = "text")
    private String content;

    private String imageUrl;
    private boolean published = true;
    private Instant publishedAt = Instant.now();

    @PrePersist
    @PreUpdate
    void ensureSlug() {
        if (slug == null || slug.isBlank()) slug = SlugUtil.slugify(title);
    }
}
