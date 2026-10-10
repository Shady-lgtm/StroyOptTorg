package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.ProductCardDto;
import com.example.stroyopttorg.service.FavoriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Избранное */
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping
    public List<ProductCardDto> list() {
        return favoriteService.list();
    }

    @GetMapping("/ids")
    public List<Long> ids() {
        return favoriteService.ids();
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> add(@PathVariable Long productId) {
        favoriteService.add(productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long productId) {
        favoriteService.remove(productId);
        return ResponseEntity.noContent().build();
    }
}
