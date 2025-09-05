package io.paymeter.assessment.infrastructure.adapter;


import io.paymeter.assessment.domain.model.Ticket;
import io.paymeter.assessment.domain.model.valueobject.Money;
import io.paymeter.assessment.domain.model.valueobject.Pricing;
import io.paymeter.assessment.infrastructure.adapter.entity.TicketEntity;
import io.paymeter.assessment.infrastructure.adapter.exceptions.DurationException;
import io.paymeter.assessment.infrastructure.adapter.exceptions.MoneyException;
import io.paymeter.assessment.infrastructure.adapter.exceptions.PricingException;
import io.paymeter.assessment.infrastructure.adapter.repository.ITicketRepository;
import io.paymeter.assessment.infrastructure.adapter.repository.PricingRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketsSpringJpaAdapterTest {

    @Mock
    private ITicketRepository ticketRepository;

    @Mock
    private PricingRepositoryImpl pricingRepository;

    @Mock
    private Pricing pricing;

    @InjectMocks
    private TicketsSpringJpaAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket_ShouldReturnTicket_WhenPricingAndDurationAreValid() {

        String from = "2025-09-03T19:45:00.123";
        String until = "2025-09-03T21:45:00.123";

        // Arrange
        Ticket request = Ticket.builder()
                .parkingId("PARK123")
                .fromTime(from)   // ahora es String
                .untilTime(until) // ahora es String
                .build();

        when(pricingRepository.get("PARK123")).thenReturn(pricing);
        when(pricing.calculatePrice(any(Duration.class))).thenReturn(new Money(5000));

        // Act
        Ticket result = adapter.createTicket(request);

        // Assert
        assertNotNull(result);
        assertEquals("PARK123", result.getParkingId());
        assertEquals(120L, result.getDurationMinutes()); // 2 horas exactas
        assertEquals("5000 EUR", result.getPrice());

        verify(ticketRepository, times(1)).save(any(TicketEntity.class));
        verify(pricing, times(1)).calculatePrice(any(Duration.class));
    }

    @Test
    void createTicket_ShouldThrowPricingException_WhenPricingNotFound() {

        String from = "2025-09-03T19:45:00.123";
        String until = "2025-09-03T21:45:00.123";

        // Arrange
        Ticket request = Ticket.builder()
                .parkingId("INVALID")
                .fromTime(from)
                .untilTime(until)
                .build();

        when(pricingRepository.get("INVALID")).thenReturn(null);

        // Act & Assert
        assertThrows(PricingException.class, () -> adapter.createTicket(request));
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void createTicket_ShouldThrowDurationException_WhenDurationIsNegative() {

        String from = "2025-09-03T21:45:00.123";
        String until = "2025-09-03T19:45:00.123"; // until < from

        // Arrange
        Ticket request = Ticket.builder()
                .parkingId("PARK123")
                .fromTime(from)
                .untilTime(until) // invertido
                .build();

        when(pricingRepository.get("PARK123")).thenReturn(pricing);

        // Act & Assert
        assertThrows(DurationException.class, () -> adapter.createTicket(request));
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void createTicket_ShouldThrowMoneyException_WhenCalculatePriceFails() {

        String from = "2025-09-03T19:45:00.123";
        String until = "2025-09-03T21:45:00.123";

        // Arrange
        Ticket request = Ticket.builder()
                .parkingId("PARK123")
                .fromTime(from)
                .untilTime(until)
                .build();

        when(pricingRepository.get("PARK123")).thenReturn(pricing);
        when(pricing.calculatePrice(any(Duration.class))).thenThrow(new RuntimeException("Error"));

        // Act & Assert
        assertThrows(MoneyException.class, () -> adapter.createTicket(request));
        verify(ticketRepository, never()).save(any());
    }
}
