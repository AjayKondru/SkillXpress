package com.skillxpress.skillxpress_api.controller;
import com.skillxpress.skillxpress_api.dto.CreateTeacherRequest;
import com.skillxpress.skillxpress_api.dto.TeacherProfile;
import com.skillxpress.skillxpress_api.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeacherController {
    private final TeacherService svc;

    @PostMapping
    public TeacherProfile add(@RequestBody @Valid CreateTeacherRequest req) {
        return svc.createTeacher(req);
    }

  /*  @GetMapping
    public List<TeacherProfile> all() { return svc.allTeachers(); }

    @PatchMapping("/{id}/disable")
    public void disable(@PathVariable String id) { svc.disable(id); }*/
}
