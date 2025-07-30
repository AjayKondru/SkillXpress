package com.skillxpress.skillxpress_api.service;

import com.skillxpress.skillxpress_api.dto.ChildCreateRequest;
import com.skillxpress.skillxpress_api.model.Child;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParentService {
    private final UserRepository userRepo;

    public Child addChild(User parent, ChildCreateRequest req) {
        Child child = Child.builder()
                .id(UUID.randomUUID().toString())
                .name(req.name())
                .grade(req.grade())
                .timeZone(req.timeZone())
                .build();

        parent.getParentProfile().getChildren().add(child);
        userRepo.save(parent);
        return child;
    }
}
