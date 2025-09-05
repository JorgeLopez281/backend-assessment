package io.paymeter.assessment.infrastructure.adapter.repository;

import io.paymeter.assessment.domain.model.constant.MessageConstant;
import io.paymeter.assessment.domain.model.valueobject.Pricing;
import io.paymeter.assessment.domain.repository.PricingRepository;
import io.paymeter.assessment.infrastructure.adapter.exceptions.PricingException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class PricingRepositoryImpl implements PricingRepository {

    public static final String P000123 = "P000123";
    public static final String P000456 = "P000456";

    @Override
    public Pricing get(String parkingId) {
        return switch (parkingId) {
            case P000123 -> new Pricing(parkingId,
                    2,
                    15,
                    null,
                    false
            );
            case P000456 -> new Pricing(parkingId,
                    3,
                    null,
                    20,
                    true
            );
            default ->  throw new PricingException(HttpStatus.NOT_FOUND,
                    String.format(MessageConstant.GET_PRICING_CONFIGURATION, parkingId));
        };
    }
}
