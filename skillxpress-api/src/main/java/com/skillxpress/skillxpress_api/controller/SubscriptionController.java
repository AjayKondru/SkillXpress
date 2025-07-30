package com.skillxpress.skillxpress_api.controller;

import com.skillxpress.skillxpress_api.dto.SubscriptionRequest;
import com.skillxpress.skillxpress_api.model.Subscription;
import com.skillxpress.skillxpress_api.repository.SubscriptionRepository;
import com.skillxpress.skillxpress_api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionRepository repo;
    private final PaymentService paySvc;

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody SubscriptionRequest req) throws Exception {
        // create pending sub
        Subscription sub = Subscription.builder()
                .childId(req.childId())
                .classId(req.classId())
                .amount(1500)
                .status(Subscription.Status.PAST_DUE)
                .build();
        sub = repo.save(sub);

        if ("RAZORPAY".equalsIgnoreCase(req.paymentGateway())) {
            Map<String,Object> resp = paySvc.createRazorpayOrder(sub.getId(), sub.getAmount());
            return ResponseEntity.ok(resp);
        } else if ("STRIPE".equalsIgnoreCase(req.paymentGateway())) {
            String url = paySvc.createStripeSession(sub.getId(), (long)sub.getAmount()*100, "usd");
            return ResponseEntity.ok(Map.of("checkoutUrl", url));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Unsupported gateway"));
        }
    }
}

