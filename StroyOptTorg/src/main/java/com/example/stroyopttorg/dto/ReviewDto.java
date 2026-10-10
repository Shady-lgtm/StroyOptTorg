package com.example.stroyopttorg.dto;

import java.time.Instant;

public record ReviewDto(Long id, String author, int rating, String text, Instant createdAt) {
}
