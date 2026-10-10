package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.*;
import com.example.stroyopttorg.exception.BadRequestException;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.Brand;
import com.example.stroyopttorg.model.Category;
import com.example.stroyopttorg.model.Product;
import com.example.stroyopttorg.repository.BrandRepository;
import com.example.stroyopttorg.repository.CategoryRepository;
import com.example.stroyopttorg.repository.ProductRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCatalogService {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    // ---------- Categories ----------

    @Transactional
    public CategoryDto createCategory(CategoryRequest req) {
        Category c = new Category();
        applyCategory(c, req);
        return toDto(categoryRepository.save(c));
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryRequest req) {
        Category c = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        applyCategory(c, req);
        return toDto(categoryRepository.save(c));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Категория не найдена");
        }
        categoryRepository.deleteById(id);
        categoryRepository.flush(); // FK xatosi shu yerda 409 bo'lib qaytishi uchun
    }

    private void applyCategory(Category c, CategoryRequest req) {
        c.setName(req.name());
        if (req.slug() != null && !req.slug().isBlank()) {
            c.setSlug(req.slug().trim());
        }
        c.setImageUrl(req.imageUrl());
        c.setSortOrder(req.sortOrder() == null ? 0 : req.sortOrder());
        if (req.parentId() == null) {
            c.setParent(null);
        } else {
            Category parent = categoryRepository.findById(req.parentId())
                    .orElseThrow(() -> new NotFoundException("Родительская категория не найдена"));
            // sikl bo'lmasligi uchun yuqoriga yurib tekshiramiz
            for (Category p = parent; p != null; p = p.getParent()) {
                if (c.getId() != null && c.getId().equals(p.getId())) {
                    throw new BadRequestException("Категория не может быть вложена сама в себя");
                }
            }
            c.setParent(parent);
        }
    }

    private CategoryDto toDto(Category c) {
        return new CategoryDto(c.getId(), c.getName(), c.getSlug(), c.getImageUrl(),
                c.getParent() == null ? null : c.getParent().getId(), List.of());
    }

    // ---------- Brands ----------

    @Transactional
    public BrandDto createBrand(BrandRequest req) {
        Brand b = new Brand();
        applyBrand(b, req);
        return CatalogService.toBrandDto(brandRepository.save(b));
    }

    @Transactional
    public BrandDto updateBrand(Long id, BrandRequest req) {
        Brand b = brandRepository.findById(id).orElseThrow(() -> new NotFoundException("Бренд не найден"));
        applyBrand(b, req);
        return CatalogService.toBrandDto(brandRepository.save(b));
    }

    @Transactional
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new NotFoundException("Бренд не найден");
        }
        brandRepository.deleteById(id);
        brandRepository.flush();
    }

    private void applyBrand(Brand b, BrandRequest req) {
        b.setName(req.name());
        if (req.slug() != null && !req.slug().isBlank()) {
            b.setSlug(req.slug().trim());
        }
        b.setLogoUrl(req.logoUrl());
    }

    // ---------- Products ----------

    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        return ProductMapper.toDto(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар не найден")));
    }

    @Transactional
    public ProductDto createProduct(ProductRequest req) {
        Product p = new Product();
        applyProduct(p, req);
        return ProductMapper.toDto(productRepository.save(p));
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductRequest req) {
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар не найден"));
        applyProduct(p, req);
        return ProductMapper.toDto(productRepository.save(p));
    }

    /** Buyurtmalarda ishlatilgani uchun o'chirish o'rniga faolsizlantiriladi. */
    @Transactional
    public void deactivateProduct(Long id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар не найден"));
        p.setActive(false);
    }

    private void applyProduct(Product p, ProductRequest req) {
        p.setName(req.name());
        if (req.slug() != null && !req.slug().isBlank()) {
            p.setSlug(req.slug().trim());
        }
        p.setSku(req.sku() == null || req.sku().isBlank() ? null : req.sku().trim());
        p.setDescription(req.description());
        p.setPrice(req.price());
        p.setOldPrice(req.oldPrice());
        p.setStock(req.stock() == null ? 0 : req.stock());
        p.setCategory(req.categoryId() == null ? null : categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new NotFoundException("Категория не найдена")));
        p.setBrand(req.brandId() == null ? null : brandRepository.findById(req.brandId())
                .orElseThrow(() -> new NotFoundException("Бренд не найден")));
        p.setImages(req.images() == null ? new ArrayList<>() : new ArrayList<>(req.images()));
        p.setSpecifications(req.specifications() == null ? new LinkedHashMap<>() : new LinkedHashMap<>(req.specifications()));
        p.setNewArrival(Boolean.TRUE.equals(req.newArrival()));
        p.setHit(Boolean.TRUE.equals(req.hit()));
        p.setActive(req.active() == null || req.active());
    }
}
