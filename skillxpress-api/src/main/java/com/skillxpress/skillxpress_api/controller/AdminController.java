package com.skillxpress.skillxpress_api.controller;

import com.skillxpress.skillxpress_api.model.ClassSession;
import com.skillxpress.skillxpress_api.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ClassSessionRepository classRepo;

    @PostMapping("/class")
    public ClassSession createClass(@RequestBody ClassSession cs) {
        return classRepo.save(cs);
    }

    @PatchMapping("/class/{id}")
    public ClassSession updateClass(@PathVariable String id, @RequestBody ClassSession cs) {
        cs.setId(id); return classRepo.save(cs);
    }
}
