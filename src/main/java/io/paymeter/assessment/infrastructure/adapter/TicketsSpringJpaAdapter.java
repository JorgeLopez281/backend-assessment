package io.paymeter.assessment.infrastructure.adapter;

import io.paymeter.assessment.domain.model.Ticket;
import io.paymeter.assessment.domain.model.constant.MessageConstant;
import io.paymeter.assessment.domain.model.valueobject.Money;
import io.paymeter.assessment.domain.model.valueobject.Pricing;
import io.paymeter.assessment.domain.port.ITicketsPort;
import io.paymeter.assessment.infrastructure.adapter.entity.TicketEntity;
import io.paymeter.assessment.infrastructure.adapter.exceptions.DurationException;
import io.paymeter.assessment.infrastructure.adapter.exceptions.MoneyException;
import io.paymeter.assessment.infrastructure.adapter.exceptions.PricingException;
import io.paymeter.assessment.infrastructure.adapter.repository.ITicketRepository;
import io.paymeter.assessment.infrastructure.adapter.repository.PricingRepositoryImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional
public class TicketsSpringJpaAdapter implements ITicketsPort {

    private final ITicketRepository ticketRepository;
    private final PricingRepositoryImpl pricingRepository;

    @Autowired
    public TicketsSpringJpaAdapter(ITicketRepository ticketRepository, PricingRepositoryImpl pricingRepository) {
        this.ticketRepository = ticketRepository;
        this.pricingRepository = pricingRepository;
    }

    @Override
    public Ticket createTicket(Ticket request) {

        Pricing pricing = pricingRepository.get(request.getParkingId());
        if (pricing == null) {
            throw new PricingException(HttpStatus.NOT_FOUND,
                    String.format(MessageConstant.GET_PRICING_CONFIGURATION, request.getParkingId()));
        }

        LocalDateTime from = LocalDateTime.parse(request.getFromTime());
        LocalDateTime to = request.getUntilTime() != null ? LocalDateTime.parse(request.getUntilTime()) : LocalDateTime.now();
        Duration duration = Duration.between(from, to);
        Money price;

        if (duration.isNegative()) {
            throw new DurationException(HttpStatus.BAD_REQUEST,
                    String.format(MessageConstant.DURATION_IS_NEGATIVE_ERROR));
        }

        try {
            price = pricing.calculatePrice(duration);
        } catch (Exception e) {
            throw new MoneyException(HttpStatus.CONFLICT,
                    String.format(MessageConstant.MONEY_CALCULATION_ERROR));
        }

        TicketEntity ticketEntity =
                TicketEntity.builder()
                        .parkingId(request.getParkingId())
                        .fromTime(request.getFromTime())
                        .untilTime(request.getUntilTime())
                        .durationMinutes(duration.toMinutes())
                        .priceCents(price.getAmount()).
                        build();

        ticketRepository.save(ticketEntity);

        return Ticket.builder()
                .parkingId(request.getParkingId())
                .fromTime(request.getFromTime())
                .untilTime(request.getUntilTime())
                .durationMinutes(duration.toMinutes())
                .price(price.getAmount() + " " + price.getCurrencyCode())
                .build();
    }
}
