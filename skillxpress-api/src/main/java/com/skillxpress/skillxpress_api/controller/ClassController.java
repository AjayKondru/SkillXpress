package com.skillxpress.skillxpress_api.controller;

import com.skillxpress.skillxpress_api.dto.EnrolRequest;
import com.skillxpress.skillxpress_api.dto.RatingRequest;
import com.skillxpress.skillxpress_api.model.ClassSession;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.service.AccessService;
import com.skillxpress.skillxpress_api.service.BookingService;
import com.skillxpress.skillxpress_api.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {
    private final ClassService classService;
    private final AccessService accessService;
    private final BookingService bookingService;

    // ── 1. search listing ──────────────────────────────────────────────
    @GetMapping("/search")
    public List<ClassSession> search(@RequestParam int grade,
                                     @RequestParam String subject) {
        return classService.search(grade, subject);
    }

    // ── 2. link for enrolled users (student/parent/tutor) ───────────────
    @GetMapping("/{id}/joinLink")
    public Map<String, String> joinLink(@PathVariable String id,
                                        @AuthenticationPrincipal User user) {
        if (!accessService.canJoinClass(user, id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enrolled");
        return Map.of("url", "https://meet.jit.si/skillxpress-" + id);
    }

    // ── 3. optional tutor-only link ─────────────────────────────────────
    @GetMapping("/{id}/startLink")
    public Map<String, String> startLink(@PathVariable String id,
                                         @AuthenticationPrincipal User user) {
        if (!accessService.isTutorForClass(user, id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not this class’s tutor");
        return Map.of("url", "https://meet.jit.si/skillxpress-" + id);
    }

    // ── 4. rating stub (unchanged) ──────────────────────────────────────
    @PostMapping("/{id}/rating")
    public ResponseEntity<Void> rate(@PathVariable String id,
                                     @RequestBody RatingRequest req) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/enrol")
    public ResponseEntity<Map<String,Object>> enrol(@PathVariable String id,
                                                    @Valid @RequestBody EnrolRequest req,
                                                    @AuthenticationPrincipal User user) throws Exception {

        Map<String,Object> resp = bookingService.enrol(id, req, user);
        return ResponseEntity.ok(resp);
    }
}
