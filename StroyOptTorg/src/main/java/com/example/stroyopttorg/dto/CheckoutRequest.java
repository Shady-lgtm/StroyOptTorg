package com.example.stroyopttorg.dto;

import com.example.stroyopttorg.model.DeliveryMethod;
import com.example.stroyopttorg.model.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Оформление заказа */
public record CheckoutRequest(@NotBlank String customerName, @NotBlank String phone, @Email String email,
                              String city, String address, @NotNull DeliveryMethod deliveryMethod,
                              @NotNull PaymentMethod paymentMethod, String comment) {
}
