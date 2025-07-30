package com.skillxpress.skillxpress_api.controller;

import com.skillxpress.skillxpress_api.dto.DashboardClassDto;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.service.DashboardService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService svc;

    @GetMapping("/classes")
    public List<DashboardClassDto> upcoming(@AuthenticationPrincipal User user) {
        return svc.getUpcoming(user);
    }
}
