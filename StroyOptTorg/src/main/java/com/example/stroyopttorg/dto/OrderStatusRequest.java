package com.example.stroyopttorg.dto;

import com.example.stroyopttorg.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(@NotNull OrderStatus status) {
}
