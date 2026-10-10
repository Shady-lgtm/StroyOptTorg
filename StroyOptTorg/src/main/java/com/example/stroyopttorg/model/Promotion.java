package com.example.stroyopttorg.model;

import com.example.stroyopttorg.util.SlugUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Акции */
@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(columnDefinition = "text")
    private String description;

    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active = true;

    @PrePersist
    @PreUpdate
    void ensureSlug() {
        if (slug == null || slug.isBlank()) slug = SlugUtil.slugify(title);
    }
}
