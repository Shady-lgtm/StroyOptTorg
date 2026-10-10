package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.Order;
import com.example.stroyopttorg.model.OrderStatus;
import com.example.stroyopttorg.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "items")
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
