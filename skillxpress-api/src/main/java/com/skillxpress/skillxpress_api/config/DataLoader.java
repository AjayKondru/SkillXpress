package com.skillxpress.skillxpress_api.config;

 import com.skillxpress.skillxpress_api.model.*;
 import com.skillxpress.skillxpress_api.repository.ClassSessionRepository;
 import com.skillxpress.skillxpress_api.repository.UserRepository;
 import lombok.RequiredArgsConstructor;
 import org.springframework.boot.CommandLineRunner;
 import org.springframework.context.annotation.Profile;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.stereotype.Component;

 import java.time.LocalDateTime;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepo;
    private final ClassSessionRepository classRepo;
    private final PasswordEncoder encoder;
    @Override public void run(String... args) {
        if (userRepo.count() > 0) return;       // already filled

        // Parent + child
        var child = Child.builder()
                .id("child1")
                .name("Avi")
                .grade(3)
                .timeZone("Asia/Kolkata")
                .build();
        var parent = User.builder()
                .email("parent@test.com")
                .passwordHash(encoder.encode("password"))  // = "password"
                .role(User.Role.PARENT)
                .parentProfile(ParentProfile.builder()
                        .children(List.of(child)).build())
                .enabled(true)
                .build();
        userRepo.save(parent);

        // Tutor
        var tutor = User.builder()
                .email("tutor@test.com")
                .passwordHash(encoder.encode("password")) // "password"
                .role(User.Role.TUTOR)
                .tutorProfile(TutorProfile.builder()
                        .fullName("Mr. Sharma")
                        .subjects(List.of("Math"))
                        .grades(List.of(3))
                        .bio("CBSE expert").build())
                .enabled(true)
                .build();
        userRepo.save(tutor);

        // Tutor
        seedAdmin();

        // Class (future time)
        var classId = "cls123";
        classRepo.save(ClassSession.builder()
                .id(classId)
                .subject("Math")
                .grade(3)
                .tutorId(tutor.getId())
                .capacity(4)
                .studentIds(new ArrayList<>())      // empty
                .startTime(LocalDateTime.now().plusDays(1).withHour(18))
                .endTime(LocalDateTime.now().plusDays(1).withHour(19))
                .build());
    }

    private void seedAdmin() {
        final String email = "admin@skillxpress.com";
        if (userRepo.existsByEmail(email)) {

            return;
        }

        User admin = User.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .passwordHash(encoder.encode("admin123"))
                .role(User.Role.ADMIN)
                .enabled(true)
                .firstLogin(false)
                .build();

        userRepo.save(admin);

    }
}

