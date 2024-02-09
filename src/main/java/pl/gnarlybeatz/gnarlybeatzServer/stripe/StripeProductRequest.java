package pl.gnarlybeatz.gnarlybeatzServer.stripe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeProductRequest {
    private String productStripeId;
    private String standardProductStripePriceId;
    private String deluxeProductStripePriceId;
    private String licenseType;
    private String userId;
}
