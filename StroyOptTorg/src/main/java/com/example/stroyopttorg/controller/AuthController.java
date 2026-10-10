package com.example.stroyopttorg.controller;

import com.example.stroyopttorg.dto.JwtResponse;
import com.example.stroyopttorg.dto.LoginRequest;
import com.example.stroyopttorg.dto.SignupRequest;
import com.example.stroyopttorg.exception.BadRequestException;
import com.example.stroyopttorg.model.User;
import com.example.stroyopttorg.repository.UserRepository;
import com.example.stroyopttorg.security.JwtUtils;
import com.example.stroyopttorg.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/** Авторизация / Регистрация */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public JwtResponse signin(@Valid @RequestBody LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        return toResponse((UserDetails) authentication.getPrincipal());
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse signup(@Valid @RequestBody SignupRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new BadRequestException("Это имя пользователя уже занято");
        }
        String email = req.email() == null || req.email().isBlank() ? null : req.email().trim();
        if (email != null && userRepository.existsByEmail(email)) {
            throw new BadRequestException("Этот email уже зарегистрирован");
        }
        // Rol har doim ROLE_USER. Admin faqat DB / ADMIN_USERNAME orqali yaratiladi.
        User user = User.builder()
                .username(req.username())
                .email(email)
                .password(encoder.encode(req.password()))
                .fullName(req.fullName())
                .phone(req.phone())
                .companyName(req.companyName())
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
        return toResponse(userDetailsService.loadUserByUsername(user.getUsername()));
    }

    private JwtResponse toResponse(UserDetails ud) {
        String role = ud.getAuthorities().iterator().next().getAuthority();
        return new JwtResponse(jwtUtils.generateJwtToken(ud), ud.getUsername(), role);
    }
}
