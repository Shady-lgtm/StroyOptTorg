package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.HomeDto;
import com.example.stroyopttorg.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Главная страница */
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping
    public HomeDto home() {
        return homeService.home();
    }
}
