package com.skillxpress.skillxpress_api.service;


import com.skillxpress.skillxpress_api.dto.EnrolRequest;
import com.skillxpress.skillxpress_api.model.Subscription;
import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.repository.ClassSessionRepository;
import com.skillxpress.skillxpress_api.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final ClassSessionRepository classRepo;
    private final SubscriptionRepository subRepo;
    private final PaymentService paymentSvc;

    /** enrol child, create pending Subscription, return payment checkout details */
    @Transactional
    public Map<String, Object> enrol(String classId, EnrolRequest req, User caller) throws Exception {

        // 1. validate child belongs to caller (or caller *is* the student)
        String studentId = validateStudentLink(caller, req.childId());

        // 2. add student to class if seats free
        boolean added = classRepo.addStudentIfSpace(classId, studentId);
        if (!added) throw new IllegalStateException("Class is full");

        // 3. create pending subscription
        Subscription sub = Subscription.builder()
                .childId(studentId)
                .classId(classId)
                .amount(1500)                          // todo: variable price
                .status(Subscription.Status.PAST_DUE)
                .build();
        sub = subRepo.save(sub);

        // 4. kick off payment
        if ("RAZORPAY".equalsIgnoreCase(req.paymentGateway())) {
            Map<String,Object> razor = paymentSvc.createRazorpayOrder(sub.getId(), sub.getAmount());
            return Map.of(
                    "subscriptionId", sub.getId(),
                    "gateway", "RAZORPAY",
                    "order", razor);
        }
        if ("STRIPE".equalsIgnoreCase(req.paymentGateway())) {
            String url = paymentSvc.createStripeSession(sub.getId(),
                    (long)sub.getAmount()*100, "usd");
            return Map.of(
                    "subscriptionId", sub.getId(),
                    "gateway", "STRIPE",
                    "checkoutUrl", url);
        }
        throw new IllegalArgumentException("Unsupported paymentGateway");
    }

    /** helper: ensure parent owns child or caller is that student */
    private String validateStudentLink(User caller, String childId) {
        return switch (caller.getRole()) {
            case STUDENT -> {
                if (caller.getId().equals(childId))
                    yield childId;                         // success case
                throw new IllegalStateException("Student mismatch");
            }
            case PARENT  -> caller.getParentProfile().getChildren().stream()
                    .filter(c -> c.getId().equals(childId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Child not found"))
                    .getId();
            default -> throw new IllegalStateException("Only parents/students can enrol");
        };
    }
}

