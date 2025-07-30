package com.skillxpress.skillxpress_api.controller;

import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.skillxpress.skillxpress_api.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pay/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {
    private final PaymentService paySvc;

    @Value("${razorpay.webhookSecret}") private String razorWebhookSecret;
    @Value("${stripe.webhookSecret}") private String stripeWebhookSecret;

    @PostMapping("/razorpay")
    public ResponseEntity<Void> razorpayHook(@RequestBody String payload, @RequestHeader("X-Razorpay-Signature") String sig) throws RazorpayException {
        if (!Utils.verifyWebhookSignature(payload, sig, razorWebhookSecret)) return ResponseEntity.status(400).build();
        var event = new org.json.JSONObject(payload);
        if ("payment.authorized".equals(event.getString("event"))) {
            String subId = event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity").getString("receipt");
            paySvc.activateSubscription(subId);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stripe")
    public ResponseEntity<Void> stripeHook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sig) throws Exception {
        Event evt;
        try {
            evt = Webhook.constructEvent(payload, sig, stripeWebhookSecret);
        } catch (SignatureVerificationException e) {return ResponseEntity.status(400).build();}
        if ("checkout.session.completed".equals(evt.getType())) {
            Session session = (Session) evt.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);          // v24+

            if (session != null && session.getMetadata() != null) {
                String subId = session.getMetadata().get("subscriptionId");
                paySvc.activateSubscription(subId);
            }
        }
        return ResponseEntity.ok().build();
    }
}
