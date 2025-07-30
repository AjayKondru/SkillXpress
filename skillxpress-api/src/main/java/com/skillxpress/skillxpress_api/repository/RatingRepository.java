package com.skillxpress.skillxpress_api.repository;

import com.skillxpress.skillxpress_api.dto.TutorRatingAggregate;
import com.skillxpress.skillxpress_api.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    TutorRatingAggregate aggregateByTutor(String tutorId);
}
