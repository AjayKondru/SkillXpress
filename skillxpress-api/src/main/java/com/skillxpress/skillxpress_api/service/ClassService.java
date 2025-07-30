package com.skillxpress.skillxpress_api.service;

import com.skillxpress.skillxpress_api.model.ClassSession;
import com.skillxpress.skillxpress_api.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassSessionRepository repo;

    public List<ClassSession> search(int grade, String subject) {
        // TODO implement proper query
        return repo.findAll().stream()
                .filter(c -> c.getGrade()==grade && c.getSubject().equalsIgnoreCase(subject))
                .toList();
    }
}
