package com.skillxpress.skillxpress_api.service;

import com.skillxpress.skillxpress_api.dto.*;
import com.skillxpress.skillxpress_api.model.*;
import com.skillxpress.skillxpress_api.repository.*;
import com.skillxpress.skillxpress_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest req) {


        if (userRepo.existsByEmail(req.email())) throw new IllegalStateException("Email in use");
        List<Child> kids = req.children() == null
                ? List.of()
                : req.children().stream()
                .map(dto -> Child.builder()
                        .id(UUID.randomUUID().toString())   // or dto.id() if supplied
                        .name(dto.name())
                        .grade(dto.grade())
                        .timeZone(dto.timeZone())
                        .build())
                .collect(Collectors.toList());

        ParentProfile pp = ParentProfile.builder()
                .children(kids)
                .build();

        User user = User.builder()
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .role(User.Role.PARENT)
                .parentProfile(pp)            // never null now
                .enabled(true)
                .build();
        userRepo.save(user);

        List<String> childIds = user.getParentProfile() == null
                ? List.of()
                : user.getParentProfile().getChildren().stream()
                .map(Child::getId)
                .toList();

        return new AuthResponse(jwtUtil.generateToken(user), childIds);
    }

    public String login(LoginRequest req) {
        var user = userRepo.findByEmail(req.email()).orElseThrow();
        if (!encoder.matches(req.password(), user.getPasswordHash())) throw new IllegalStateException("Bad creds");
        return jwtUtil.generateToken(user);
    }
}
