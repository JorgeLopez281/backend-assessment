package io.paymeter.assessment.application.service;

import io.paymeter.assessment.application.mapper.TicketDtoMapper;
import io.paymeter.assessment.application.usecase.ITicketService;
import io.paymeter.assessment.domain.model.request.TicketRequest;
import io.paymeter.assessment.domain.model.response.TicketResponse;
import io.paymeter.assessment.domain.port.ITicketsPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService implements ITicketService {

    private final ITicketsPort ticketsPort;
    private final TicketDtoMapper ticketDtoMapper;

    @Autowired
    public TicketService(ITicketsPort ticketsPort, TicketDtoMapper ticketDtoMapper) {
        this.ticketsPort = ticketsPort;
        this.ticketDtoMapper = ticketDtoMapper;
    }

    @Override
    public TicketResponse createTicket(TicketRequest request) {
        var ticketToCalculate = ticketDtoMapper.toDomain(request);
        var ticketCalculated = ticketsPort.createTicket(ticketToCalculate);

        return ticketDtoMapper.toDto(ticketCalculated);
    }
}
