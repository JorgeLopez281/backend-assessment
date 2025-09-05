package io.paymeter.assessment.domain.port;

import io.paymeter.assessment.domain.model.Ticket;

public interface ITicketsPort {

    Ticket createTicket(Ticket request);
}
