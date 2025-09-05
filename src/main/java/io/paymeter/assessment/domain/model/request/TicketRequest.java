package io.paymeter.assessment.domain.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {

    @NotBlank(message = "parkingId cannot be empty")
    @Size(min = 1, max = 30, message = "ParkingId must not exceed 30 characters")
    private String parkingId;

    @NotNull(message = "Entry date (from) cannot be null")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{3})?(Z|[+-]\\d{2}:\\d{2})?$",
            message = "must be a valid ISO 8601 date (Ej. 2025-09-03T21:45:00.000)"
    )
    private String from;

    @NotNull(message = "Checkout date (to) cannot be null")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{3})?(Z|[+-]\\d{2}:\\d{2})?$",
            message = "must be a valid ISO 8601 date (Ej. 2025-09-03T21:45:00.000)"
    )
    private String to;
}
