package com.example.stroyopttorg.model;

import com.example.stroyopttorg.util.SlugUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private String imageUrl;
    private int sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @PrePersist
    @PreUpdate
    void ensureSlug() {
        if (slug == null || slug.isBlank()) slug = SlugUtil.slugify(name);
    }
}
