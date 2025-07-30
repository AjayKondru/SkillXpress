package com.skillxpress.skillxpress_api.repository;

import com.skillxpress.skillxpress_api.dto.TutorRatingAggregate;
import com.skillxpress.skillxpress_api.model.Rating;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;


public class RatingRepositoryImpl implements RatingRepositoryCustom {
    private final MongoTemplate mongo;
    public RatingRepositoryImpl(MongoTemplate m){ this.mongo = m; }

    @Override
    public TutorRatingAggregate aggregateByTutor(String tutorId){
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("tutorId").is(tutorId)),
                group().avg("stars").as("avg").count().as("cnt")
        );
        var result = mongo.aggregate(agg, Rating.class, Document.class)
                .getUniqueMappedResult();
        return new TutorRatingAggregate(
                result.getDouble("avg"),
                result.getLong("cnt"));
    }


}
