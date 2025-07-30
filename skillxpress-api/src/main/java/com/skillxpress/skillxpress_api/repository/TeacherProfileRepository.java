package com.skillxpress.skillxpress_api.repository;

import com.skillxpress.skillxpress_api.dto.TeacherProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeacherProfileRepository
        extends MongoRepository<TeacherProfile, String> {

    boolean existsByPhone(String phone);
    // plus any custom queries you need later
}
