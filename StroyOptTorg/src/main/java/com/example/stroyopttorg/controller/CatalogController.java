package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.*;
import com.example.stroyopttorg.service.CatalogService;
import com.example.stroyopttorg.service.ReviewService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Каталог: категории, бренды, товары, карточка товара, отзывы */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;
    private final ReviewService reviewService;

    @GetMapping("/categories")
    public List<CategoryDto> categories() {
        return catalogService.categoryTree();
    }

    @GetMapping("/categories/{idOrSlug}")
    public CategoryDto category(@PathVariable String idOrSlug) {
        return catalogService.getCategory(idOrSlug);
    }

    @GetMapping("/brands")
    public List<BrandDto> brands() {
        return catalogService.brands();
    }

    /**
     * Katalog + filtr + qidiruv.
     * category - id yoki slug (ichki kategoriyalar ham kiradi); sort: new | price_asc | price_desc | rating | name
     */
    @GetMapping("/products")
    public PageResponse<ProductCardDto> products(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<Long> brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Boolean onSale,
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        ProductFilter f = new ProductFilter(category, brand, minPrice, maxPrice, q, inStock, onSale);
        return catalogService.search(f, page, size, sort, false);
    }

    /** Qidiruv qatori uchun tezkor takliflar */
    @GetMapping("/products/suggest")
    public List<ProductCardDto> suggest(@RequestParam String q) {
        ProductFilter f = new ProductFilter(null, null, null, null, q, null, null);
        return catalogService.search(f, 0, 6, "rating", false).content();
    }

    @GetMapping("/products/{idOrSlug}")
    public ProductDto product(@PathVariable String idOrSlug) {
        return catalogService.getProduct(idOrSlug);
    }

    @GetMapping("/products/{id}/related")
    public List<ProductCardDto> related(@PathVariable Long id) {
        return catalogService.related(id);
    }

    @GetMapping("/products/{id}/reviews")
    public PageResponse<ReviewDto> reviews(@PathVariable Long id,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return reviewService.list(id, page, size);
    }

    @PostMapping("/products/{id}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto addReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest req) {
        return reviewService.add(id, req);
    }
}
