package io.paymeter.assessment.domain.model.constant;

public class MessageConstant {
    public static final String GET_PRICING_CONFIGURATION = "No pricing configuration exists for parking lot ID %s. Please verify the parking lot identifier.";
    public static final String DURATION_IS_NEGATIVE_ERROR = "The calculated duration between 'fromTime' and 'untilTime' is negative. Please verify that the exit time is not before the entry time.";
    public static final String MONEY_CALCULATION_ERROR = "An error occurred while calculating the price for the parked time. Please verify the input values or try again later.";
}
