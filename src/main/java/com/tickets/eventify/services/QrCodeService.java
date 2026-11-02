package com.tickets.eventify.services;

import com.tickets.eventify.domain.entities.QrCode;
import com.tickets.eventify.domain.entities.Ticket;
import java.util.UUID;

public interface QrCodeService {

  QrCode generateQrCode(Ticket ticket);

  byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
