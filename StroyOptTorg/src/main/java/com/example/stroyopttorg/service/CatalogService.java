package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.*;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.Brand;
import com.example.stroyopttorg.model.Category;
import com.example.stroyopttorg.model.Product;
import com.example.stroyopttorg.repository.BrandRepository;
import com.example.stroyopttorg.repository.CategoryRepository;
import com.example.stroyopttorg.repository.ProductRepository;
import com.example.stroyopttorg.repository.ProductSpecs;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    // ---------- Категории ----------

    @Transactional(readOnly = true)
    public List<CategoryDto> categoryTree() {
        List<Category> all = categoryRepository.findAllByOrderBySortOrderAscNameAsc();
        Map<Long, List<Category>> byParent = new HashMap<>();
        List<Category> roots = new ArrayList<>();
        for (Category c : all) {
            if (c.getParent() == null) {
                roots.add(c);
            } else {
                byParent.computeIfAbsent(c.getParent().getId(), k -> new ArrayList<>()).add(c);
            }
        }
        return roots.stream().map(c -> toTree(c, byParent)).toList();
    }

    private CategoryDto toTree(Category c, Map<Long, List<Category>> byParent) {
        List<CategoryDto> kids = byParent.getOrDefault(c.getId(), List.of()).stream()
                .map(k -> toTree(k, byParent)).toList();
        return new CategoryDto(c.getId(), c.getName(), c.getSlug(), c.getImageUrl(),
                c.getParent() == null ? null : c.getParent().getId(), kids);
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategory(String idOrSlug) {
        Category c = findCategory(idOrSlug);
        List<CategoryDto> kids = categoryRepository.findAllByOrderBySortOrderAscNameAsc().stream()
                .filter(x -> x.getParent() != null && x.getParent().getId().equals(c.getId()))
                .map(x -> new CategoryDto(x.getId(), x.getName(), x.getSlug(), x.getImageUrl(), c.getId(), List.of()))
                .toList();
        return new CategoryDto(c.getId(), c.getName(), c.getSlug(), c.getImageUrl(),
                c.getParent() == null ? null : c.getParent().getId(), kids);
    }

    private Category findCategory(String idOrSlug) {
        Optional<Category> c = isNumeric(idOrSlug)
                ? categoryRepository.findById(Long.parseLong(idOrSlug))
                : categoryRepository.findBySlug(idOrSlug);
        return c.orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }

    private Set<Long> descendantIds(Long rootId) {
        Map<Long, List<Long>> children = new HashMap<>();
        for (Category c : categoryRepository.findAll()) {
            if (c.getParent() != null) {
                children.computeIfAbsent(c.getParent().getId(), k -> new ArrayList<>()).add(c.getId());
            }
        }
        Set<Long> result = new HashSet<>();
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(rootId);
        while (!stack.isEmpty()) {
            Long id = stack.pop();
            if (result.add(id)) {
                for (Long child : children.getOrDefault(id, List.of())) {
                    stack.push(child);
                }
            }
        }
        return result;
    }

    // ---------- Бренды ----------

    @Transactional(readOnly = true)
    public List<BrandDto> brands() {
        return brandRepository.findAllByOrderByNameAsc().stream().map(CatalogService::toBrandDto).toList();
    }

    public static BrandDto toBrandDto(Brand b) {
        return new BrandDto(b.getId(), b.getName(), b.getSlug(), b.getLogoUrl());
    }

    // ---------- Товары ----------

    @Transactional(readOnly = true)
    public PageResponse<ProductCardDto> search(ProductFilter f, int page, int size, String sort,
                                               boolean includeInactive) {
        Set<Long> categoryIds = null;
        if (f.category() != null && !f.category().isBlank()) {
            categoryIds = descendantIds(findCategory(f.category()).getId());
        }
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), sortOf(sort));
        Page<Product> result = productRepository.findAll(ProductSpecs.filter(f, categoryIds, includeInactive), pageable);
        return PageResponse.of(result.map(ProductMapper::toCard));
    }

    private Sort sortOf(String sort) {
        Sort base = switch (sort == null ? "" : sort) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "rating" -> Sort.by("rating").descending();
            case "name" -> Sort.by("name").ascending();
            default -> Sort.by("createdAt").descending();
        };
        return base.and(Sort.by("id").descending());
    }

    @Transactional(readOnly = true)
    public ProductDto getProduct(String idOrSlug) {
        Optional<Product> p = isNumeric(idOrSlug)
                ? productRepository.findByIdAndActiveTrue(Long.parseLong(idOrSlug))
                : productRepository.findBySlugAndActiveTrue(idOrSlug);
        return p.map(ProductMapper::toDto).orElseThrow(() -> new NotFoundException("Товар не найден"));
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> related(Long productId) {
        Product p = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new NotFoundException("Товар не найден"));
        if (p.getCategory() == null) {
            return List.of();
        }
        return productRepository
                .findTop8ByCategoryIdAndActiveTrueAndIdNotOrderByRatingDesc(p.getCategory().getId(), p.getId())
                .stream().map(ProductMapper::toCard).toList();
    }

    private static boolean isNumeric(String s) {
        return s != null && s.length() <= 18 && s.matches("\\d+");
    }
}
