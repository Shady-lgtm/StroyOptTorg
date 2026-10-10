package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.CompareDto;
import com.example.stroyopttorg.service.CompareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Сравнение товаров */
@RestController
@RequestMapping("/api/compare")
@RequiredArgsConstructor
public class CompareController {
    private final CompareService compareService;

    @GetMapping
    public CompareDto get() {
        return compareService.get();
    }

    @PostMapping("/{productId}")
    public CompareDto add(@PathVariable Long productId) {
        return compareService.add(productId);
    }

    @DeleteMapping("/{productId}")
    public CompareDto remove(@PathVariable Long productId) {
        return compareService.remove(productId);
    }

    @DeleteMapping
    public ResponseEntity<Void> clear() {
        compareService.clear();
        return ResponseEntity.noContent().build();
    }
}
