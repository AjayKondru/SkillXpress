package com.skillxpress.skillxpress_api.service;

import com.skillxpress.skillxpress_api.dto.AvailableSlot;
import com.skillxpress.skillxpress_api.dto.CreateTeacherRequest;
import com.skillxpress.skillxpress_api.dto.SlotDto;
import com.skillxpress.skillxpress_api.dto.TeacherProfile;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.repository.TeacherProfileRepository;
import com.skillxpress.skillxpress_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final UserRepository userRepo;
    private final TeacherProfileRepository profileRepo;
    private final PasswordEncoder encoder;
    private final JavaMailSender mail;

    /* ------------------------------------------------------------------ */
    /*  1. CREATE TEACHER                                                 */
    /* ------------------------------------------------------------------ */
    @Transactional
    public TeacherProfile createTeacher(CreateTeacherRequest req) {
        if (userRepo.existsByEmail(req.email()))
            throw new IllegalStateException("E-mail already in use");

        String otp =  UUID.randomUUID().toString();
        String id  = UUID.randomUUID().toString();

        // User account
        userRepo.save(User.builder()
                .id(id)
                .email(req.email())
                .passwordHash(encoder.encode(otp))
                .role(User.Role.TUTOR)
                .enabled(true)
                .firstLogin(true)              // force change-password
                .build());

        // Teacher profile
        TeacherProfile tp = mapToProfile(req, id);
        profileRepo.save(tp);

        sendCredentials(req.email(), otp, req.name());
        return tp;
    }

    /* ------------------------------------------------------------------ */
    /*  2. LIST & SOFT-DELETE                                             */
    /* ------------------------------------------------------------------ */
    public List<TeacherProfile> allTeachers() {
        return profileRepo.findAll();
    }

    public void disable(String id) {
        profileRepo.findById(id).ifPresent(tp -> {
            tp.setEnabled(false);
            profileRepo.save(tp);
        });
        userRepo.findById(id).ifPresent(u -> {
            u.setEnabled(false);
            userRepo.save(u);
        });
    }

    /* ------------------------------------------------------------------ */
    /*  3. HELPER METHODS                                                 */
    /* ------------------------------------------------------------------ */
    private TeacherProfile mapToProfile(CreateTeacherRequest r, String id) {
        List<AvailableSlot> slots = r.availableTimes().stream()
                .map(this::toSlot)
                .toList();

        return TeacherProfile.builder()
                .id(id)
                .name(r.name())
                .qualification(r.qualification())
                .phone(r.phone())
                .eligibleGrades(Set.copyOf(r.grades()))
                .eligibleSubjects(Set.copyOf(r.subjects()))
                .availableTimes(slots)
                .enabled(true)
                .build();
    }

    private AvailableSlot toSlot(SlotDto dto) {
        return new AvailableSlot(
                DayOfWeek.valueOf(dto.day()),          // "MONDAY"
                LocalTime.parse(dto.start()),          // "08:00"
                LocalTime.parse(dto.end())             // "16:00"
        );
    }

    private void sendCredentials(String to, String otp, String name) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(to);
        m.setSubject("Your SkillXpress tutor account");
        m.setText("""
                Hi %s,
                                
                Welcome to SkillXpress! Your tutor account is ready.
                                
                • Login email: %s
                • Temporary password: %s
                                
                Please log in and change this password immediately.
                                
                Regards,
                SkillXpress Team
                """.formatted(name, to, otp));
        mail.send(m);
    }
}
