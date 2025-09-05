package io.paymeter.assessment.application.mapper;


import io.paymeter.assessment.domain.model.Ticket;
import io.paymeter.assessment.domain.model.request.TicketRequest;
import io.paymeter.assessment.domain.model.response.TicketResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TicketDtoMapperTest {
    private TicketDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TicketDtoMapper.class);
    }

    @Test
    void toDomain_ShouldMapRequestToDomain() {
        // Arrange
        TicketRequest request = new TicketRequest();
        request.setParkingId("PARK123");
        request.setFrom("2025-09-03T19:45:00.123");  // String en formato ISO
        request.setTo("2025-09-03T21:45:00.123");

        // Act
        Ticket ticket = mapper.toDomain(request);

        // Assert
        assertNotNull(ticket);
        assertEquals("PARK123", ticket.getParkingId());
        assertEquals(request.getFrom(), ticket.getFromTime());
        assertEquals(request.getTo(), ticket.getUntilTime());
    }

    @Test
    void toDto_ShouldMapDomainToResponse() {
        // Arrange
        String from = "2025-09-03T19:45:00.123";
        String until = "2025-09-03T21:45:00.123";

        Ticket ticket = Ticket.builder()
                .parkingId("PARK123")
                .fromTime(from)
                .untilTime(until)
                .durationMinutes(120L)
                .price("5000 COP")
                .build();

        // Act
        TicketResponse response = mapper.toDto(ticket);

        // Assert
        assertNotNull(response);
        assertEquals("PARK123", response.getParkingId());
        assertEquals(from.toString(), response.getFrom());   // ahora es String
        assertEquals(until.toString(), response.getUntil()); // ahora es String
        assertEquals(120L, response.getDurationMinutes());
        assertEquals("5000 COP", response.getPrice());
    }
}
