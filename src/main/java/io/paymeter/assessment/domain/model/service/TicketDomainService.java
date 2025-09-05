package io.paymeter.assessment.domain.model.service;

import io.paymeter.assessment.domain.model.Ticket;
import io.paymeter.assessment.domain.model.constant.MessageConstant;
import io.paymeter.assessment.domain.model.valueobject.Money;
import io.paymeter.assessment.domain.model.valueobject.Pricing;
import io.paymeter.assessment.infrastructure.adapter.exceptions.DurationException;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class TicketDomainService {

    public Money calculatePrice(Ticket ticket, Pricing pricing) {
        LocalDateTime from = LocalDateTime.parse(ticket.getFromTime());
        LocalDateTime to = ticket.getUntilTime() != null
                ? LocalDateTime.parse(ticket.getUntilTime())
                : LocalDateTime.now();

        Duration duration = Duration.between(from, to);

        if (duration.isNegative()) {
            throw new DurationException(HttpStatus.BAD_REQUEST,
                    String.format(MessageConstant.DURATION_IS_NEGATIVE_ERROR));
        }

        return pricing.calculatePrice(duration);
    }
}