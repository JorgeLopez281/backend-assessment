package io.paymeter.assessment.domain.repository;

import io.paymeter.assessment.domain.model.valueobject.Pricing;

public interface PricingRepository {
    Pricing get(String parkingId);
}
