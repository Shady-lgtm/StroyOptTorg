package com.example.stroyopttorg.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /** Buyurtma vaqtidagi nusxa (keyin narx/nom o'zgarsa ham saqlanadi) */
    private String productName;
    private String sku;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    private int quantity;
}
