package com.skillxpress.skillxpress_api.repository;

import com.mongodb.client.result.UpdateResult;
import com.skillxpress.skillxpress_api.model.ClassSession;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ClassSessionRepositoryImpl implements ClassSessionRepositoryCustom {
    private final MongoTemplate mongo;
    public ClassSessionRepositoryImpl(MongoTemplate mongo){ this.mongo = mongo; }

    @Override
    public boolean addStudentIfSpace(String classId, String studentId) {
        Query q = new Query(Criteria.where("_id").is(classId))
                .addCriteria( Criteria.expr(
                        MongoExpression.create(
                                "{ $lt: [ { $size: '$studentIds' }, '$capacity' ] }"
                        )));
        Update u = new Update().push("studentIds", studentId);
        UpdateResult res = mongo.updateFirst( q, u, ClassSession.class);
        return res.getModifiedCount() == 1;
    }
}