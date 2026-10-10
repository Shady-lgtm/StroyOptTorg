package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.ChangePasswordRequest;
import com.example.stroyopttorg.dto.MessageResponse;
import com.example.stroyopttorg.dto.UpdateProfileRequest;
import com.example.stroyopttorg.dto.UserProfileDto;
import com.example.stroyopttorg.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** Личный кабинет: профиль */
@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserProfileDto me() {
        return userService.me();
    }

    @PutMapping
    public UserProfileDto update(@Valid @RequestBody UpdateProfileRequest req) {
        return userService.update(req);
    }

    @PutMapping("/password")
    public MessageResponse changePassword(@Valid @RequestBody ChangePasswordRequest req) {
        userService.changePassword(req);
        return new MessageResponse("Пароль изменён");
    }
}
