package com.skillxpress.skillxpress_api.repository;

import com.skillxpress.skillxpress_api.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    Optional<Subscription> findByChildIdAndClassIdAndStatus(
            String childId, String classId, Subscription.Status status);

    List<Subscription> findByNextBillingDateBeforeAndStatus(
            LocalDateTime time, Subscription.Status status);
}
