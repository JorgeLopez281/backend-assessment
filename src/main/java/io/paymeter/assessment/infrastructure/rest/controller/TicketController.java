package io.paymeter.assessment.infrastructure.rest.controller;

import io.paymeter.assessment.application.usecase.ITicketService;
import io.paymeter.assessment.domain.model.request.TicketRequest;
import io.paymeter.assessment.domain.model.response.ErrorResponse;
import io.paymeter.assessment.domain.model.response.TicketResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/tickets")
@Tag(name = "Ticket Controller",
		description = "This is an API responsible for calculating the value of the time that was parked and saving it in the DB")
public class TicketController {

	private final ITicketService ticketService;

	@Autowired
    public TicketController(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			summary = "Create a parking ticket",
			responses = {
					@ApiResponse(responseCode = "200", description = "Ticket created successfully",
							content = @Content(schema = @Schema(implementation = TicketResponse.class))),
					@ApiResponse(responseCode = "400", description = "Invalid request",
							content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
			},
			description = "Calculates the parking cost and stores a new ticket in the database"
	)
	public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest request) {
		TicketResponse response = ticketService.createTicket(request);
		return ResponseEntity.ok(response);
	}
}
