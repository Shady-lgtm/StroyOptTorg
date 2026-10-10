package com.example.stroyopttorg.controller.admin;

import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.BlogPost;
import com.example.stroyopttorg.model.Promotion;
import com.example.stroyopttorg.model.StaticPage;
import com.example.stroyopttorg.repository.BlogPostRepository;
import com.example.stroyopttorg.repository.PromotionRepository;
import com.example.stroyopttorg.repository.StaticPageRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Admin: blog, aksiyalar va statik sahifalar (О компании, Доставка, Оплата, Контакты...) */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminContentController {
    private final BlogPostRepository blogPostRepository;
    private final PromotionRepository promotionRepository;
    private final StaticPageRepository staticPageRepository;

    // ----- blog -----
    @GetMapping("/blog")
    public List<BlogPost> blogList() {
        return blogPostRepository.findAll(Sort.by("publishedAt").descending());
    }

    @PostMapping("/blog")
    @ResponseStatus(HttpStatus.CREATED)
    public BlogPost blogCreate(@Valid @RequestBody BlogPost body) {
        body.setId(null);
        return blogPostRepository.save(body);
    }

    @PutMapping("/blog/{id}")
    public BlogPost blogUpdate(@PathVariable Long id, @Valid @RequestBody BlogPost body) {
        BlogPost p = blogPostRepository.findById(id).orElseThrow(() -> new NotFoundException("Статья не найдена"));
        p.setTitle(body.getTitle());
        if (body.getSlug() != null && !body.getSlug().isBlank()) p.setSlug(body.getSlug());
        p.setExcerpt(body.getExcerpt());
        p.setContent(body.getContent());
        p.setImageUrl(body.getImageUrl());
        p.setPublished(body.isPublished());
        return blogPostRepository.save(p);
    }

    @DeleteMapping("/blog/{id}")
    public ResponseEntity<Void> blogDelete(@PathVariable Long id) {
        blogPostRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ----- promotions -----
    @GetMapping("/promotions")
    public List<Promotion> promoList() {
        return promotionRepository.findAll(Sort.by("id").descending());
    }

    @PostMapping("/promotions")
    @ResponseStatus(HttpStatus.CREATED)
    public Promotion promoCreate(@Valid @RequestBody Promotion body) {
        body.setId(null);
        return promotionRepository.save(body);
    }

    @PutMapping("/promotions/{id}")
    public Promotion promoUpdate(@PathVariable Long id, @Valid @RequestBody Promotion body) {
        Promotion p = promotionRepository.findById(id).orElseThrow(() -> new NotFoundException("Акция не найдена"));
        p.setTitle(body.getTitle());
        if (body.getSlug() != null && !body.getSlug().isBlank()) p.setSlug(body.getSlug());
        p.setDescription(body.getDescription());
        p.setImageUrl(body.getImageUrl());
        p.setStartDate(body.getStartDate());
        p.setEndDate(body.getEndDate());
        p.setActive(body.isActive());
        return promotionRepository.save(p);
    }

    @DeleteMapping("/promotions/{id}")
    public ResponseEntity<Void> promoDelete(@PathVariable Long id) {
        promotionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ----- static pages -----
    @GetMapping("/pages")
    public List<StaticPage> pageList() {
        return staticPageRepository.findAll(Sort.by("slug"));
    }

    @PostMapping("/pages")
    @ResponseStatus(HttpStatus.CREATED)
    public StaticPage pageCreate(@Valid @RequestBody StaticPage body) {
        body.setId(null);
        return staticPageRepository.save(body);
    }

    @PutMapping("/pages/{id}")
    public StaticPage pageUpdate(@PathVariable Long id, @Valid @RequestBody StaticPage body) {
        StaticPage p = staticPageRepository.findById(id).orElseThrow(() -> new NotFoundException("Страница не найдена"));
        p.setSlug(body.getSlug());
        p.setTitle(body.getTitle());
        p.setContent(body.getContent());
        return staticPageRepository.save(p);
    }

    @DeleteMapping("/pages/{id}")
    public ResponseEntity<Void> pageDelete(@PathVariable Long id) {
        staticPageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
