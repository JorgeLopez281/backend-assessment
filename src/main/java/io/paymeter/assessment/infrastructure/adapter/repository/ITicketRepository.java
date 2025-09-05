package io.paymeter.assessment.infrastructure.adapter.repository;

import io.paymeter.assessment.infrastructure.adapter.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITicketRepository extends JpaRepository<TicketEntity, Long> {
}
