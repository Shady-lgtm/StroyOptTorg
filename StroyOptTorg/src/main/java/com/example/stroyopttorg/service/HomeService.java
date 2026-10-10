package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.BlogSummaryDto;
import com.example.stroyopttorg.dto.HomeDto;
import com.example.stroyopttorg.model.BlogPost;
import com.example.stroyopttorg.model.Promotion;
import com.example.stroyopttorg.repository.BlogPostRepository;
import com.example.stroyopttorg.repository.ProductRepository;
import com.example.stroyopttorg.repository.PromotionRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final CatalogService catalogService;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final BlogPostRepository blogPostRepository;

    @Transactional(readOnly = true)
    public HomeDto home() {
        return new HomeDto(
                catalogService.categoryTree(),
                productRepository.findTop8ByActiveTrueAndHitTrueOrderByIdDesc().stream().map(ProductMapper::toCard).toList(),
                productRepository.findTop8ByActiveTrueAndNewArrivalTrueOrderByCreatedAtDesc().stream().map(ProductMapper::toCard).toList(),
                productRepository.findTop8ByActiveTrueAndOldPriceIsNotNullOrderByIdDesc().stream().map(ProductMapper::toCard).toList(),
                currentPromotions(),
                blogPostRepository.findTop3ByPublishedTrueOrderByPublishedAtDesc().stream().map(HomeService::toSummary).toList());
    }

    public List<Promotion> currentPromotions() {
        LocalDate today = LocalDate.now();
        return promotionRepository.findByActiveTrueOrderByIdDesc().stream()
                .filter(p -> (p.getStartDate() == null || !p.getStartDate().isAfter(today))
                        && (p.getEndDate() == null || !p.getEndDate().isBefore(today)))
                .toList();
    }

    public static BlogSummaryDto toSummary(BlogPost b) {
        return new BlogSummaryDto(b.getId(), b.getTitle(), b.getSlug(), b.getExcerpt(), b.getImageUrl(), b.getPublishedAt());
    }
}
