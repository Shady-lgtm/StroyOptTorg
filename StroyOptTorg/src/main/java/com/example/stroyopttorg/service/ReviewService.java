package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.PageResponse;
import com.example.stroyopttorg.dto.ReviewDto;
import com.example.stroyopttorg.dto.ReviewRequest;
import com.example.stroyopttorg.exception.BadRequestException;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.Product;
import com.example.stroyopttorg.model.Review;
import com.example.stroyopttorg.model.User;
import com.example.stroyopttorg.repository.ProductRepository;
import com.example.stroyopttorg.repository.ReviewRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public PageResponse<ReviewDto> list(Long productId, int page, int size) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Товар не найден");
        }
        return PageResponse.of(reviewRepository
                .findByProductIdOrderByCreatedAtDesc(productId, PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50)))
                .map(this::toDto));
    }

    @Transactional
    public ReviewDto add(Long productId, ReviewRequest req) {
        User user = currentUser.require();
        Product product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new NotFoundException("Товар не найден"));
        if (reviewRepository.existsByProductIdAndUserId(productId, user.getId())) {
            throw new BadRequestException("Вы уже оставили отзыв на этот товар");
        }
        Review r = new Review();
        r.setProduct(product);
        r.setUser(user);
        r.setRating(req.rating());
        r.setText(req.text());
        reviewRepository.saveAndFlush(r);

        Double avg = reviewRepository.averageRating(productId);
        product.setRating(BigDecimal.valueOf(avg == null ? 0 : avg).setScale(1, RoundingMode.HALF_UP).doubleValue());
        product.setReviewsCount((int) reviewRepository.countByProductId(productId));
        productRepository.save(product);
        return toDto(r);
    }

    private ReviewDto toDto(Review r) {
        User u = r.getUser();
        String author = u.getFullName() != null && !u.getFullName().isBlank() ? u.getFullName() : u.getUsername();
        return new ReviewDto(r.getId(), author, r.getRating(), r.getText(), r.getCreatedAt());
    }
}
