package com.example.stroyopttorg.service;

import com.example.stroyopttorg.exception.UnauthorizedException;
import com.example.stroyopttorg.model.User;
import com.example.stroyopttorg.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Joriy autentifikatsiyadan o'tgan foydalanuvchini beradi. */
@Component
@RequiredArgsConstructor
public class CurrentUser {
    private final UserRepository userRepository;

    public Optional<User> optional() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !a.isAuthenticated() || a instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return userRepository.findByUsername(a.getName());
    }

    public User require() {
        return optional().orElseThrow(() -> new UnauthorizedException("Требуется авторизация"));
    }
}
