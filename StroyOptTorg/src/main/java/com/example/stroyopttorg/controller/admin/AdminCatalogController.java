package com.example.stroyopttorg.controller.admin;

import com.example.stroyopttorg.dto.*;
import com.example.stroyopttorg.service.AdminCatalogService;
import com.example.stroyopttorg.service.CatalogService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Admin: kategoriya, brend, mahsulot boshqaruvi (faqat ROLE_ADMIN) */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminCatalogController {
    private final AdminCatalogService adminService;
    private final CatalogService catalogService;

    // ----- categories -----
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryRequest req) {
        return adminService.createCategory(req);
    }

    @PutMapping("/categories/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest req) {
        return adminService.updateCategory(id, req);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ----- brands -----
    @PostMapping("/brands")
    @ResponseStatus(HttpStatus.CREATED)
    public BrandDto createBrand(@Valid @RequestBody BrandRequest req) {
        return adminService.createBrand(req);
    }

    @PutMapping("/brands/{id}")
    public BrandDto updateBrand(@PathVariable Long id, @Valid @RequestBody BrandRequest req) {
        return adminService.updateBrand(id, req);
    }

    @DeleteMapping("/brands/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        adminService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    // ----- products -----
    @GetMapping("/products")
    public PageResponse<ProductCardDto> products(@RequestParam(required = false) String category,
                                                 @RequestParam(required = false) List<Long> brand,
                                                 @RequestParam(required = false) BigDecimal minPrice,
                                                 @RequestParam(required = false) BigDecimal maxPrice,
                                                 @RequestParam(required = false) String q,
                                                 @RequestParam(defaultValue = "new") String sort,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size) {
        ProductFilter f = new ProductFilter(category, brand, minPrice, maxPrice, q, null, null);
        return catalogService.search(f, page, size, sort, true);
    }

    @GetMapping("/products/{id}")
    public ProductDto product(@PathVariable Long id) {
        return adminService.getProduct(id);
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@Valid @RequestBody ProductRequest req) {
        return adminService.createProduct(req);
    }

    @PutMapping("/products/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        return adminService.updateProduct(id, req);
    }

    /** O'chirish = faolsizlantirish (buyurtma tarixi saqlanadi) */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        adminService.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }
}
