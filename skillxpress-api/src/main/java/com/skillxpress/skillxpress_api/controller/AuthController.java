package com.skillxpress.skillxpress_api.controller;

import com.skillxpress.skillxpress_api.dto.*;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.repository.UserRepository;
import com.skillxpress.skillxpress_api.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepo;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {

        return  authService.register(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req);
        return new LoginResponse(token);
    }

    @PostMapping("/change-password")
    public void changePwd(@AuthenticationPrincipal User tutor,
                          @RequestBody Map<String, String> body) {
        String newPw = body.get("password");
        tutor.setPasswordHash(encoder.encode(newPw));
        tutor.setFirstLogin(false); // Ensure this method exists in User
        userRepo.save(tutor);
    }
}



