package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.CompareDto;
import com.example.stroyopttorg.dto.ProductDto;
import com.example.stroyopttorg.exception.BadRequestException;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.CompareItem;
import com.example.stroyopttorg.model.Product;
import com.example.stroyopttorg.model.User;
import com.example.stroyopttorg.repository.CompareItemRepository;
import com.example.stroyopttorg.repository.ProductRepository;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Сравнение товаров (максимум 4) */
@Service
@RequiredArgsConstructor
public class CompareService {
    public static final int MAX_ITEMS = 4;

    private final CompareItemRepository compareItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public CompareDto get() {
        return build(currentUser.require());
    }

    @Transactional
    public CompareDto add(Long productId) {
        User user = currentUser.require();
        if (!compareItemRepository.existsByUserAndProductId(user, productId)) {
            if (compareItemRepository.countByUser(user) >= MAX_ITEMS) {
                throw new BadRequestException("В сравнение можно добавить не более " + MAX_ITEMS + " товаров");
            }
            Product product = productRepository.findByIdAndActiveTrue(productId)
                    .orElseThrow(() -> new NotFoundException("Товар не найден"));
            CompareItem ci = new CompareItem();
            ci.setUser(user);
            ci.setProduct(product);
            compareItemRepository.save(ci);
        }
        return build(user);
    }

    @Transactional
    public CompareDto remove(Long productId) {
        User user = currentUser.require();
        compareItemRepository.deleteByUserAndProductId(user, productId);
        return build(user);
    }

    @Transactional
    public void clear() {
        compareItemRepository.deleteByUser(currentUser.require());
    }

    private CompareDto build(User user) {
        List<ProductDto> products = compareItemRepository.findByUserOrderByIdAsc(user).stream()
                .map(ci -> ProductMapper.toDto(ci.getProduct())).toList();
        Set<String> keys = new LinkedHashSet<>();
        products.forEach(p -> keys.addAll(p.specifications().keySet()));
        return new CompareDto(products, List.copyOf(keys));
    }
}
