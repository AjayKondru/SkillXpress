package com.skillxpress.skillxpress_api.controller;

import com.skillxpress.skillxpress_api.dto.ChildCreateRequest;
import com.skillxpress.skillxpress_api.model.Child;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentSvc;

    /** Parent adds a new child */
    @PostMapping("/addchildren")
    public Child addChild(@AuthenticationPrincipal User parent,
                          @Valid @RequestBody ChildCreateRequest req) {
        if (parent.getRole() != User.Role.PARENT)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only parents");

        return parentSvc.addChild(parent, req);
    }

    /** List all children (useful for UI) */
    @GetMapping("/children")
    public List<Child> list(@AuthenticationPrincipal User parent) {
        return parent.getParentProfile().getChildren();
    }
}