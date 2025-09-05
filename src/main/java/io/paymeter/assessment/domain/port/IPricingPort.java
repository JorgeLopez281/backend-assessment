package io.paymeter.assessment.domain.port;

import io.paymeter.assessment.domain.model.valueobject.Pricing;

public interface IPricingPort {
    Pricing getPricing(String parkingId);
}
