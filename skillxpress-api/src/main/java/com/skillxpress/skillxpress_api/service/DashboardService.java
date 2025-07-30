package com.skillxpress.skillxpress_api.service;

import com.skillxpress.skillxpress_api.dto.DashboardClassDto;
import com.skillxpress.skillxpress_api.model.Child;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ClassSessionRepository repo;

    public List<DashboardClassDto> getUpcoming(User caller) {

        if (caller.getRole() == User.Role.STUDENT) {
            return repo.findUpcomingForStudent(caller.getId());
        }

        // Parent: loop over all children
        List<String> childIds = caller.getParentProfile()
                .getChildren()
                .stream()
                .map(Child::getId)
                .toList();

        return childIds.stream()
                .flatMap(id -> repo.findUpcomingForStudent(id).stream())
                .sorted(Comparator.comparing(DashboardClassDto::startTime))
                .toList();
    }
}

