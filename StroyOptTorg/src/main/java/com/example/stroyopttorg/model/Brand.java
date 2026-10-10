package com.example.stroyopttorg.model;

import com.example.stroyopttorg.util.SlugUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private String logoUrl;

    @PrePersist
    @PreUpdate
    void ensureSlug() {
        if (slug == null || slug.isBlank()) slug = SlugUtil.slugify(name);
    }
}
