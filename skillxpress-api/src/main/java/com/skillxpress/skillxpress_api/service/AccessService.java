package com.skillxpress.skillxpress_api.service;

import com.skillxpress.skillxpress_api.model.ClassSession;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final ClassSessionRepository classRepo;

    /** student / parent / tutor allowed to enter the Jitsi room? */
    public boolean canJoinClass(User user, String classId) {
        ClassSession cs = classRepo.findById(classId).orElse(null);
        if (cs == null) return false;

        return switch (user.getRole()) {
            case TUTOR   -> cs.getTutorId().equals(user.getId());
            case STUDENT -> cs.getStudentIds().contains(user.getId());
            case PARENT  -> user.getParentProfile().getChildren().stream()
                    .anyMatch(c -> cs.getStudentIds().contains(c.getId()));
            default      -> false;
        };
    }

    /** strict tutor check for /startLink */
    public boolean isTutorForClass(User user, String classId) {
        if (user.getRole() != User.Role.TUTOR) return false;
        return classRepo.findById(classId)
                .map(cs -> cs.getTutorId().equals(user.getId()))
                .orElse(false);
    }
}

