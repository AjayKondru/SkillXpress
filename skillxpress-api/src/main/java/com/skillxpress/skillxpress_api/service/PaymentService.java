package com.skillxpress.skillxpress_api.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.skillxpress.skillxpress_api.model.Subscription;
import com.skillxpress.skillxpress_api.repository.SubscriptionRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final SubscriptionRepository subRepo;

    @Value("${razorpay.key}") private String razorKey;
    @Value("${razorpay.secret}") private String razorSecret;

    @Value("${stripe.apiKey}") private String stripeKey;
    @Value("${skillxpress.domain}") private String domain;

    /** Create Razorpay order (INR) */
    public Map<String, Object> createRazorpayOrder(String subscriptionId, double amountInr) throws Exception {
        RazorpayClient client = new RazorpayClient(razorKey, razorSecret);
        JSONObject orderReq = new JSONObject();
        orderReq.put("amount", (int)(amountInr*100)); // subunits paise
        orderReq.put("currency", "INR");
        orderReq.put("receipt", subscriptionId);
        Order order = client.orders.create(orderReq);
        return Map.of("orderId", order.get("id"), "key", razorKey);
    }

    /** Create Stripe Checkout session (foreign currency) */
    public String createStripeSession(String subscriptionId, long amountCents, String currency) throws Exception {
        Stripe.apiKey = stripeKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(domain + "/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(domain + "/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(amountCents)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("SkillXpress Subscription")
                                        .build())
                                .build())
                        .build())
                .putMetadata("subscriptionId", subscriptionId)
                .build();
        Session session = Session.create(params);
        return session.getUrl();
    }

    /** Activate subscription when payment verified */
    public void activateSubscription(String subscriptionId) {
        Subscription sub = subRepo.findById(subscriptionId).orElseThrow();
        sub.setStatus(Subscription.Status.ACTIVE);
        sub.setStartDate(LocalDateTime.now());
        sub.setNextBillingDate(LocalDateTime.now().plusMonths(1));
        subRepo.save(sub);
    }
}

