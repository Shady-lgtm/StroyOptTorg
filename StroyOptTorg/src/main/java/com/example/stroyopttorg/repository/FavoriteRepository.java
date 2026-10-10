package com.example.stroyopttorg.repository;

import com.example.stroyopttorg.model.Favorite;
import com.example.stroyopttorg.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserOrderByIdDesc(User user);

    boolean existsByUserAndProductId(User user, Long productId);

    void deleteByUserAndProductId(User user, Long productId);
}
