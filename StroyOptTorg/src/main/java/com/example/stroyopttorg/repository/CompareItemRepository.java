package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.CompareItem;
import com.example.stroyopttorg.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompareItemRepository extends JpaRepository<CompareItem, Long> {
    List<CompareItem> findByUserOrderByIdAsc(User user);

    boolean existsByUserAndProductId(User user, Long productId);

    long countByUser(User user);

    void deleteByUserAndProductId(User user, Long productId);

    void deleteByUser(User user);
}
