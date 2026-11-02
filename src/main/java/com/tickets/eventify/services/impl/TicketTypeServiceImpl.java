package com.tickets.eventify.services.impl;

import com.tickets.eventify.domain.entities.Ticket;
import com.tickets.eventify.domain.entities.TicketStatusEnum;
import com.tickets.eventify.domain.entities.TicketType;
import com.tickets.eventify.domain.entities.User;
import com.tickets.eventify.exceptions.TicketTypeNotFoundException;
import com.tickets.eventify.exceptions.TicketsSoldOutException;
import com.tickets.eventify.exceptions.UserNotFoundException;
import com.tickets.eventify.repositories.TicketRepository;
import com.tickets.eventify.repositories.TicketTypeRepository;
import com.tickets.eventify.repositories.UserRepository;
import com.tickets.eventify.services.QrCodeService;
import com.tickets.eventify.services.TicketTypeService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

  private final UserRepository userRepository;
  private final TicketTypeRepository ticketTypeRepository;
  private final TicketRepository ticketRepository;
  private final QrCodeService qrCodeService;

  @Override
  @Transactional
  public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
        String.format("User with ID %s was not found", userId)
    ));

    TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
        .orElseThrow(() -> new TicketTypeNotFoundException(
            String.format("Ticket type with ID %s was not found", ticketTypeId)
        ));

    int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());
    Integer totalAvailable = ticketType.getTotalAvailable();

    if(purchasedTickets + 1 > totalAvailable) {
      throw new TicketsSoldOutException();
    }

    Ticket ticket = new Ticket();
    ticket.setStatus(TicketStatusEnum.PURCHASED);
    ticket.setTicketType(ticketType);
    ticket.setPurchaser(user);

    Ticket savedTicket = ticketRepository.save(ticket);
    qrCodeService.generateQrCode(savedTicket);

    return ticketRepository.save(savedTicket);
  }
}
