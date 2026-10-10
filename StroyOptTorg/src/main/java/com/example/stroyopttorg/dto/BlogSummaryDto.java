package com.example.stroyopttorg.dto;

import java.time.Instant;

public record BlogSummaryDto(Long id, String title, String slug, String excerpt, String imageUrl, Instant publishedAt) {
}
