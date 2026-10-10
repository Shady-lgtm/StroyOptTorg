package com.example.stroyopttorg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/protected")
    public ResponseEntity<String> getProtectedData() {
        return ResponseEntity.ok("Tabriklayman! Siz JWT token yordamida himoyalangan endpointga kirdingiz.");
    }
}
