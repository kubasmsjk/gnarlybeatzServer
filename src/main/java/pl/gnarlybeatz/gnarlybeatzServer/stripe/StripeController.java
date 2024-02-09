package pl.gnarlybeatz.gnarlybeatzServer.stripe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(
            @RequestBody List<StripeProductRequest> request
    ) {
        return ResponseEntity.ok(stripeService.checkout(request));
    }

    //stripe listen --forward-to localhost:8081/api/payments/stripe/events
    @PostMapping("/stripe/events")
    public ResponseEntity<String> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        return ResponseEntity.ok(stripeService.handleStripeEvent(payload, sigHeader));
    }
}
