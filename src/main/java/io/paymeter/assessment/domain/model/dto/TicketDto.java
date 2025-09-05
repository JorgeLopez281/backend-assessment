package io.paymeter.assessment.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private String parkingId;
    private OffsetDateTime fromTime;
    private OffsetDateTime untilTime;
    private Long durationMinutes;
    private Integer price;
}
