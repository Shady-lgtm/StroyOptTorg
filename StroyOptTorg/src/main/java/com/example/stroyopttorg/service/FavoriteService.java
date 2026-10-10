package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.ProductCardDto;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.Favorite;
import com.example.stroyopttorg.model.Product;
import com.example.stroyopttorg.model.User;
import com.example.stroyopttorg.repository.FavoriteRepository;
import com.example.stroyopttorg.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Избранное */
@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public List<ProductCardDto> list() {
        return favoriteRepository.findByUserOrderByIdDesc(currentUser.require()).stream()
                .map(f -> ProductMapper.toCard(f.getProduct())).toList();
    }

    @Transactional(readOnly = true)
    public List<Long> ids() {
        return favoriteRepository.findByUserOrderByIdDesc(currentUser.require()).stream()
                .map(f -> f.getProduct().getId()).toList();
    }

    @Transactional
    public void add(Long productId) {
        User user = currentUser.require();
        if (favoriteRepository.existsByUserAndProductId(user, productId)) {
            return;
        }
        Product product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new NotFoundException("Товар не найден"));
        Favorite f = new Favorite();
        f.setUser(user);
        f.setProduct(product);
        favoriteRepository.save(f);
    }

    @Transactional
    public void remove(Long productId) {
        favoriteRepository.deleteByUserAndProductId(currentUser.require(), productId);
    }
}
