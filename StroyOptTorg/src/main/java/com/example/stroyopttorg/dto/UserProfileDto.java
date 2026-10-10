package com.example.stroyopttorg.dto;

public record UserProfileDto(Long id, String username, String email, String fullName, String phone,
                             String address, String companyName, String role) {
}
