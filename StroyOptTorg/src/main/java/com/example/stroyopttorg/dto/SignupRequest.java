package com.example.stroyopttorg.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @Email String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        String fullName,
        String phone,
        String companyName) {
}
