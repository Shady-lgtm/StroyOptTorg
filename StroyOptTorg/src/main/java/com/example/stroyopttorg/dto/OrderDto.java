package com.example.stroyopttorg.dto;

import com.example.stroyopttorg.model.DeliveryMethod;
import com.example.stroyopttorg.model.OrderStatus;
import com.example.stroyopttorg.model.PaymentMethod;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDto(Long id, OrderStatus status, String customerName, String phone, String email, String city,
                       String address, DeliveryMethod deliveryMethod, PaymentMethod paymentMethod, String comment,
                       BigDecimal total, boolean oneClick, Instant createdAt, List<OrderItemDto> items) {
}
