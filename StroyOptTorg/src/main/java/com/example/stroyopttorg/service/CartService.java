package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.CartDto;
import com.example.stroyopttorg.dto.CartItemDto;
import com.example.stroyopttorg.exception.BadRequestException;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.CartItem;
import com.example.stroyopttorg.model.Product;
import com.example.stroyopttorg.model.User;
import com.example.stroyopttorg.repository.CartItemRepository;
import com.example.stroyopttorg.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public CartDto getCart() {
        return buildCart(currentUser.require());
    }

    @Transactional
    public CartDto addItem(Long productId, int quantity) {
        User user = currentUser.require();
        Product product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new NotFoundException("Товар не найден"));
        CartItem item = cartItemRepository.findByUserAndProductId(user, productId).orElseGet(() -> {
            CartItem ci = new CartItem();
            ci.setUser(user);
            ci.setProduct(product);
            return ci;
        });
        int newQty = item.getQuantity() + quantity;
        checkStock(product, newQty);
        item.setQuantity(newQty);
        cartItemRepository.save(item);
        return buildCart(user);
    }

    @Transactional
    public CartDto setQuantity(Long productId, int quantity) {
        User user = currentUser.require();
        CartItem item = cartItemRepository.findByUserAndProductId(user, productId)
                .orElseThrow(() -> new NotFoundException("Товара нет в корзине"));
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            checkStock(item.getProduct(), quantity);
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return buildCart(user);
    }

    @Transactional
    public CartDto removeItem(Long productId) {
        User user = currentUser.require();
        cartItemRepository.deleteByUserAndProductId(user, productId);
        return buildCart(user);
    }

    @Transactional
    public void clear() {
        cartItemRepository.deleteByUser(currentUser.require());
    }

    private void checkStock(Product p, int qty) {
        if (qty > p.getStock()) {
            throw new BadRequestException("Недостаточно товара «" + p.getName() + "» на складе (доступно: " + p.getStock() + ")");
        }
    }

    private CartDto buildCart(User user) {
        List<CartItemDto> items = cartItemRepository.findByUserOrderByIdAsc(user).stream().map(ci -> {
            Product p = ci.getProduct();
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            return new CartItemDto(p.getId(), p.getName(), p.getSlug(), p.getSku(), ProductMapper.mainImage(p),
                    p.getPrice(), ci.getQuantity(), line, p.getStock());
        }).toList();
        int totalQty = items.stream().mapToInt(CartItemDto::quantity).sum();
        BigDecimal total = items.stream().map(CartItemDto::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartDto(items, totalQty, total);
    }
}
