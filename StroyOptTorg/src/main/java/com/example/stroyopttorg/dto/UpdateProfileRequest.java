package com.example.stroyopttorg.dto;

import jakarta.validation.constraints.Email;

public record UpdateProfileRequest(@Email String email, String fullName, String phone, String address,
                                   String companyName) {
}
