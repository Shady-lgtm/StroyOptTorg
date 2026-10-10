package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.BlogSummaryDto;
import com.example.stroyopttorg.dto.CallbackRequestDto;
import com.example.stroyopttorg.dto.MessageResponse;
import com.example.stroyopttorg.dto.PageResponse;
import com.example.stroyopttorg.exception.NotFoundException;
import com.example.stroyopttorg.model.BlogPost;
import com.example.stroyopttorg.model.CallbackRequest;
import com.example.stroyopttorg.model.Promotion;
import com.example.stroyopttorg.model.StaticPage;
import com.example.stroyopttorg.repository.BlogPostRepository;
import com.example.stroyopttorg.repository.CallbackRequestRepository;
import com.example.stroyopttorg.repository.PromotionRepository;
import com.example.stroyopttorg.repository.StaticPageRepository;
import com.example.stroyopttorg.service.HomeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Блог, Акции, статические страницы (О компании, Доставка, Оплата, Контакты...), обратный звонок */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContentController {
    private final BlogPostRepository blogPostRepository;
    private final PromotionRepository promotionRepository;
    private final StaticPageRepository staticPageRepository;
    private final CallbackRequestRepository callbackRequestRepository;
    private final HomeService homeService;

    @GetMapping("/blog")
    public PageResponse<BlogSummaryDto> blog(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "9") int size) {
        return PageResponse.of(blogPostRepository
                .findByPublishedTrueOrderByPublishedAtDesc(PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50)))
                .map(HomeService::toSummary));
    }

    @GetMapping("/blog/{slug}")
    public BlogPost blogPost(@PathVariable String slug) {
        return blogPostRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new NotFoundException("Статья не найдена"));
    }

    @GetMapping("/promotions")
    public List<Promotion> promotions() {
        return homeService.currentPromotions();
    }

    @GetMapping("/promotions/{slug}")
    public Promotion promotion(@PathVariable String slug) {
        return promotionRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new NotFoundException("Акция не найдена"));
    }

    @GetMapping("/pages/{slug}")
    public StaticPage page(@PathVariable String slug) {
        return staticPageRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Страница не найдена"));
    }

    @PostMapping("/callback")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse callback(@Valid @RequestBody CallbackRequestDto req) {
        CallbackRequest c = new CallbackRequest();
        c.setName(req.name());
        c.setPhone(req.phone());
        c.setMessage(req.message());
        callbackRequestRepository.save(c);
        return new MessageResponse("Заявка принята, мы скоро свяжемся с вами");
    }
}
