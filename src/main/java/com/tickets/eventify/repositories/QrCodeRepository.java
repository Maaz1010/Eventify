package com.tickets.eventify.repositories;

import com.tickets.eventify.domain.entities.QrCode;
import com.tickets.eventify.domain.entities.QrCodeStatusEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {
  Optional<QrCode> findByTicketIdAndTicketPurchaserId(UUID ticketId, UUID ticketPurchaseId);
  Optional<QrCode> findByIdAndStatus(UUID id, QrCodeStatusEnum status);
}
