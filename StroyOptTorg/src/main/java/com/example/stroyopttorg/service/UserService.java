package com.example.stroyopttorg.service;

import com.example.stroyopttorg.dto.ChangePasswordRequest;
import com.example.stroyopttorg.dto.UpdateProfileRequest;
import com.example.stroyopttorg.dto.UserProfileDto;
import com.example.stroyopttorg.exception.BadRequestException;
import com.example.stroyopttorg.model.User;
import com.example.stroyopttorg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CurrentUser currentUser;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static UserProfileDto toDto(User u) {
        return new UserProfileDto(u.getId(), u.getUsername(), u.getEmail(), u.getFullName(), u.getPhone(),
                u.getAddress(), u.getCompanyName(), u.getRole());
    }

    @Transactional(readOnly = true)
    public UserProfileDto me() {
        return toDto(currentUser.require());
    }

    @Transactional
    public UserProfileDto update(UpdateProfileRequest req) {
        User u = currentUser.require();
        String email = req.email() == null || req.email().isBlank() ? null : req.email().trim();
        if (email != null && !email.equalsIgnoreCase(u.getEmail())
                && userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Этот email уже используется");
        }
        u.setEmail(email);
        u.setFullName(req.fullName());
        u.setPhone(req.phone());
        u.setAddress(req.address());
        u.setCompanyName(req.companyName());
        return toDto(userRepository.save(u));
    }

    @Transactional
    public void changePassword(ChangePasswordRequest req) {
        User u = currentUser.require();
        if (!passwordEncoder.matches(req.oldPassword(), u.getPassword())) {
            throw new BadRequestException("Текущий пароль указан неверно");
        }
        u.setPassword(passwordEncoder.encode(req.newPassword()));
        userRepository.save(u);
    }
}
