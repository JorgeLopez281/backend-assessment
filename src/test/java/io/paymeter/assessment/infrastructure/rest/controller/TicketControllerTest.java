package io.paymeter.assessment.infrastructure.rest.controller;

import io.paymeter.assessment.application.usecase.ITicketService;
import io.paymeter.assessment.domain.model.request.TicketRequest;
import io.paymeter.assessment.domain.model.response.TicketResponse;
import io.paymeter.assessment.infrastructure.rest.controller.TicketController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TicketControllerTest {
    @Mock
    private ITicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket_ShouldReturnOkResponse() {
        // Arrange
        TicketRequest request = new TicketRequest();

        TicketResponse expectedResponse = new TicketResponse();
        expectedResponse.setParkingId("123");
        expectedResponse.setDurationMinutes(100);

        when(ticketService.createTicket(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<TicketResponse> responseEntity =
                ticketController.createTicket(request);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(ticketService, times(1)).createTicket(request);
    }
}
