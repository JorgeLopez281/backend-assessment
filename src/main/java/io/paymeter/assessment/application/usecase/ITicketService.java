package io.paymeter.assessment.application.usecase;

import io.paymeter.assessment.domain.model.dto.TicketDto;
import io.paymeter.assessment.domain.model.request.TicketRequest;
import io.paymeter.assessment.domain.model.response.TicketResponse;

public interface ITicketService {

    TicketResponse createTicket(TicketRequest request);
}
