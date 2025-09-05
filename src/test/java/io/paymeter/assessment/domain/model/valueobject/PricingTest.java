package io.paymeter.assessment.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingTest {

    @Test
    void whenDurationLessThanOneMinute_thenFree() {
        Pricing pricing = new Pricing("PARK1", 1000, null, null, false);
        Money result = pricing.calculatePrice(Duration.ofSeconds(30));
        assertEquals(0, result.getAmount());
    }

    @Test
    void whenDurationOneHour_noCap_noFreeFirstHour_thenChargeOneHour() {
        Pricing pricing = new Pricing("PARK1", 1000, null, null, false);
        Money result = pricing.calculatePrice(Duration.ofHours(1));
        assertEquals(1000, result.getAmount());
    }

    @Test
    void whenDurationOneHour_withFreeFirstHour_thenFree() {
        Pricing pricing = new Pricing("PARK1", 1000, null, null, true);
        Money result = pricing.calculatePrice(Duration.ofHours(1));
        assertEquals(0, result.getAmount());
    }

    @Test
    void whenDurationTwoHours_withFreeFirstHour_thenChargeOneHour() {
        Pricing pricing = new Pricing("PARK1", 1000, null, null, true);
        Money result = pricing.calculatePrice(Duration.ofHours(2));
        assertEquals(1000, result.getAmount());
    }

    @Test
    void whenDurationOneDay_withDailyCap_thenApplyDailyCap() {
        Pricing pricing = new Pricing("PARK1", 1000, 1500, null, false);
        Money result = pricing.calculatePrice(Duration.ofHours(24));
        assertEquals(1500, result.getAmount());
    }

    @Test
    void whenDurationTwoDays_withDailyCap_thenApplyCapPerDay() {
        Pricing pricing = new Pricing("PARK1", 1000, 1500, null, false);
        Money result = pricing.calculatePrice(Duration.ofHours(48));
        assertEquals(3000, result.getAmount());
    }

    @Test
    void whenDurationOneDayAnd5Hours_withDailyCap_thenCapAppliedToRemainder() {
        Pricing pricing = new Pricing("PARK1", 1000, 1500, null, false);
        Money result = pricing.calculatePrice(Duration.ofHours(29)); // 1 day + 5 hours
        assertEquals(3000, result.getAmount()); // 1500 (day) + 1500 (capped remainder)
    }

    @Test
    void whenDuration12Hours_withCapPer12h_thenApplyCap() {
        Pricing pricing = new Pricing("PARK1", 1000, null, 2000, false);
        Money result = pricing.calculatePrice(Duration.ofHours(12));
        assertEquals(2000, result.getAmount());
    }

    @Test
    void whenDuration20Hours_withCapPer12h_thenRemainderCappedToo() {
        Pricing pricing = new Pricing("PARK1", 1000, null, 2000, false);
        Money result = pricing.calculatePrice(Duration.ofHours(20));
        assertEquals(2000 + 2000, result.getAmount()); // remainder 8h capped at 2000
    }
}
