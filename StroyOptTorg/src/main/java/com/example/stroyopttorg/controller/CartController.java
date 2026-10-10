package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.CartDto;
import com.example.stroyopttorg.dto.CartItemRequest;
import com.example.stroyopttorg.dto.QuantityRequest;
import com.example.stroyopttorg.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Корзина */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CartDto get() {
        return cartService.getCart();
    }

    @PostMapping("/items")
    public CartDto add(@Valid @RequestBody CartItemRequest req) {
        return cartService.addItem(req.productId(), req.quantity());
    }

    @PutMapping("/items/{productId}")
    public CartDto setQuantity(@PathVariable Long productId, @Valid @RequestBody QuantityRequest req) {
        return cartService.setQuantity(productId, req.quantity());
    }

    @DeleteMapping("/items/{productId}")
    public CartDto remove(@PathVariable Long productId) {
        return cartService.removeItem(productId);
    }

    @DeleteMapping
    public ResponseEntity<Void> clear() {
        cartService.clear();
        return ResponseEntity.noContent().build();
    }
}
