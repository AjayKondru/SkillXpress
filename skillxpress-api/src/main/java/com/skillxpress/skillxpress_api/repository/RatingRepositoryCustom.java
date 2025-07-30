package com.skillxpress.skillxpress_api.repository;

import com.skillxpress.skillxpress_api.dto.TutorRatingAggregate;

public interface RatingRepositoryCustom {
    TutorRatingAggregate aggregateByTutor(String tutorId);
}
