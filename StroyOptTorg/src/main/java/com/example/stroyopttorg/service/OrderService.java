package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.*;
import com.example.stroyopttorg.exception.BadRequestException;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.*;
import com.example.stroyopttorg.repository.CartItemRepository;
import com.example.stroyopttorg.repository.OrderRepository;
import com.example.stroyopttorg.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUser currentUser;

    /** Оформление заказа из корзины. Javob "Спасибо" sahifasi uchun ishlatiladi. */
    @Transactional
    public OrderDto checkout(CheckoutRequest req) {
        User user = currentUser.require();
        List<CartItem> cart = cartItemRepository.findByUserOrderByIdAsc(user);
        if (cart.isEmpty()) {
            throw new BadRequestException("Корзина пуста");
        }
        if (req.deliveryMethod() != DeliveryMethod.PICKUP && (req.address() == null || req.address().isBlank())) {
            throw new BadRequestException("Укажите адрес доставки");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(req.customerName());
        order.setPhone(req.phone());
        order.setEmail(req.email());
        order.setCity(req.city());
        order.setAddress(req.address());
        order.setDeliveryMethod(req.deliveryMethod());
        order.setPaymentMethod(req.paymentMethod());
        order.setComment(req.comment());

        for (CartItem ci : cart) {
            addLine(order, ci.getProduct().getId(), ci.getQuantity());
        }
        orderRepository.save(order);
        cartItemRepository.deleteByUser(user);
        return toDto(order);
    }

    /** Купить в 1 клик (mehmon ham buyurtma bera oladi). */
    @Transactional
    public OrderDto oneClick(OneClickRequest req) {
        Order order = new Order();
        currentUser.optional().ifPresent(order::setUser);
        order.setCustomerName(req.name() == null || req.name().isBlank() ? "Покупка в 1 клик" : req.name());
        order.setPhone(req.phone());
        order.setDeliveryMethod(DeliveryMethod.PICKUP);
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setOneClick(true);
        addLine(order, req.productId(), req.quantity());
        orderRepository.save(order);
        return toDto(order);
    }

    private void addLine(Order order, Long productId, int quantity) {
        Product p = productRepository.findByIdForUpdate(productId)
                .filter(Product::isActive)
                .orElseThrow(() -> new NotFoundException("Товар не найден"));
        if (p.getStock() < quantity) {
            throw new BadRequestException("Недостаточно товара «" + p.getName() + "» на складе (доступно: " + p.getStock() + ")");
        }
        p.setStock(p.getStock() - quantity);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(p);
        item.setProductName(p.getName());
        item.setSku(p.getSku());
        item.setUnitPrice(p.getPrice());
        item.setQuantity(quantity);
        order.getItems().add(item);
        order.setTotal(order.getTotal().add(p.getPrice().multiply(BigDecimal.valueOf(quantity))));
    }

    @Transactional(readOnly = true)
    public List<OrderDto> myOrders() {
        return orderRepository.findByUserOrderByCreatedAtDesc(currentUser.require()).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public OrderDto myOrder(Long id) {
        User user = currentUser.require();
        Order o = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (o.getUser() == null || !o.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Заказ не найден");
        }
        return toDto(o);
    }

    // ---------- Admin ----------

    @Transactional(readOnly = true)
    public PageResponse<OrderDto> adminList(OrderStatus status, int page, int size) {
        PageRequest pr = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100),
                Sort.by("createdAt").descending());
        var result = status == null ? orderRepository.findAll(pr) : orderRepository.findByStatus(status, pr);
        return PageResponse.of(result.map(this::toDto));
    }

    @Transactional(readOnly = true)
    public OrderDto adminGet(Long id) {
        return toDto(orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Заказ не найден")));
    }

    @Transactional
    public OrderDto updateStatus(Long id, OrderStatus newStatus) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        OrderStatus old = o.getStatus();
        if (old == newStatus) {
            return toDto(o);
        }
        if (old == OrderStatus.CANCELLED) {
            throw new BadRequestException("Отменённый заказ нельзя восстановить");
        }
        if (newStatus == OrderStatus.CANCELLED) {
            for (OrderItem item : o.getItems()) {
                if (item.getProduct() != null) {
                    productRepository.findByIdForUpdate(item.getProduct().getId())
                            .ifPresent(p -> p.setStock(p.getStock() + item.getQuantity()));
                }
            }
        }
        o.setStatus(newStatus);
        return toDto(o);
    }

    private OrderDto toDto(Order o) {
        List<OrderItemDto> items = o.getItems().stream().map(i -> new OrderItemDto(
                i.getProduct() == null ? null : i.getProduct().getId(), i.getProductName(), i.getSku(),
                i.getUnitPrice(), i.getQuantity(),
                i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))).toList();
        return new OrderDto(o.getId(), o.getStatus(), o.getCustomerName(), o.getPhone(), o.getEmail(), o.getCity(),
                o.getAddress(), o.getDeliveryMethod(), o.getPaymentMethod(), o.getComment(), o.getTotal(),
                o.isOneClick(), o.getCreatedAt(), items);
    }
}
