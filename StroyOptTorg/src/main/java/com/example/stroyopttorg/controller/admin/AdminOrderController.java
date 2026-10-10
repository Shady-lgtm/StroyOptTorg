package com.example.stroyopttorg.controller.admin;

import com.example.stroyopttorg.dto.OrderDto;
import com.example.stroyopttorg.dto.OrderStatusRequest;
import com.example.stroyopttorg.dto.PageResponse;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.CallbackRequest;
import com.example.stroyopttorg.model.OrderStatus;
import com.example.stroyopttorg.repository.CallbackRequestRepository;
import com.example.stroyopttorg.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/** Admin: buyurtmalar va qayta qo'ng'iroq so'rovlari */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;
    private final CallbackRequestRepository callbackRequestRepository;

    @GetMapping("/orders")
    public PageResponse<OrderDto> orders(@RequestParam(required = false) OrderStatus status,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return orderService.adminList(status, page, size);
    }

    @GetMapping("/orders/{id}")
    public OrderDto order(@PathVariable Long id) {
        return orderService.adminGet(id);
    }

    @PatchMapping("/orders/{id}/status")
    public OrderDto updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusRequest req) {
        return orderService.updateStatus(id, req.status());
    }

    @GetMapping("/callbacks")
    public List<CallbackRequest> callbacks() {
        return callbackRequestRepository.findAll(Sort.by("createdAt").descending());
    }

    @PatchMapping("/callbacks/{id}/processed")
    public CallbackRequest markProcessed(@PathVariable Long id) {
        CallbackRequest c = callbackRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));
        c.setProcessed(true);
        return callbackRequestRepository.save(c);
    }
}
