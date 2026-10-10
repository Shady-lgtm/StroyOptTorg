package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.CheckoutRequest;
import com.example.stroyopttorg.dto.OneClickRequest;
import com.example.stroyopttorg.dto.OrderDto;
import com.example.stroyopttorg.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Оформление заказа, "Купить в 1 клик", заказы в личном кабинете */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto checkout(@Valid @RequestBody CheckoutRequest req) {
        return orderService.checkout(req);
    }

    @PostMapping("/one-click")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto oneClick(@Valid @RequestBody OneClickRequest req) {
        return orderService.oneClick(req);
    }

    @GetMapping
    public List<OrderDto> myOrders() {
        return orderService.myOrders();
    }

    @GetMapping("/{id}")
    public OrderDto myOrder(@PathVariable Long id) {
        return orderService.myOrder(id);
    }
}
