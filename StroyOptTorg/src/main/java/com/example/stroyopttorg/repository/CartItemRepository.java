package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.CartItem;
import com.example.stroyopttorg.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserOrderByIdAsc(User user);

    Optional<CartItem> findByUserAndProductId(User user, Long productId);

    void deleteByUserAndProductId(User user, Long productId);

    void deleteByUser(User user);
}
