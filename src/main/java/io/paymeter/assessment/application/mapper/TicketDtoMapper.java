package io.paymeter.assessment.application.mapper;

import io.paymeter.assessment.domain.model.Ticket;
import io.paymeter.assessment.domain.model.request.TicketRequest;
import io.paymeter.assessment.domain.model.response.TicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketDtoMapper {

    @Mapping(source = "parkingId", target = "parkingId")
    @Mapping(source = "from", target = "fromTime")
    @Mapping(source = "to", target = "untilTime")
    Ticket toDomain(TicketRequest request);

    @Mapping(source = "parkingId", target = "parkingId")
    @Mapping(source = "fromTime", target = "from")
    @Mapping(source = "untilTime", target = "until")
    @Mapping(source = "durationMinutes", target = "durationMinutes")
    @Mapping(source = "price", target = "price")
    TicketResponse toDto(Ticket ticket);
}
