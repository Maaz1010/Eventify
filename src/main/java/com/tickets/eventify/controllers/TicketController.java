package com.tickets.eventify.controllers;

import static com.tickets.eventify.util.JwtUtil.parseUserId;

import com.tickets.eventify.domain.dtos.GetTicketResponseDto;
import com.tickets.eventify.domain.dtos.ListTicketResponseDto;
import com.tickets.eventify.exceptions.TicketNotFoundException;
import com.tickets.eventify.mappers.TicketMapper;
import com.tickets.eventify.services.QrCodeService;
import com.tickets.eventify.services.TicketService;
import java.time.Duration;
import java.util.UUID;

import com.tickets.eventify.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

  private final TicketService ticketService;
  private final TicketTypeService ticketTypeService; // has purchaseTicket method
  private final TicketMapper ticketMapper;
  private final QrCodeService qrCodeService;

  @GetMapping
  public Page<ListTicketResponseDto> listTickets(
      @AuthenticationPrincipal Jwt jwt,
      Pageable pageable
  ) {
    return ticketService.listTicketsForUser(
        parseUserId(jwt),
        pageable
    ).map(ticketMapper::toListTicketResponseDto);
  }

  @GetMapping(path = "/{ticketId}")
  public ResponseEntity<GetTicketResponseDto> getTicket(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID ticketId
  ) {
    return ticketService
        .getTicketForUser(parseUserId(jwt), ticketId)
        .map(ticketMapper::toGetTicketResponseDto)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
  }

  /// This endpoint is for purchasing a ticket for a specific ticket type.
  /// TODO: Return the ticket details in the response body with a 201 Created status code
  @PostMapping(path = "/{ticketTypeId}")
  public ResponseEntity<GetTicketResponseDto> purchaseTicket(
          @AuthenticationPrincipal Jwt jwt,
          @PathVariable UUID ticketTypeId) {
    ticketTypeService.purchaseTicket(parseUserId(jwt), ticketTypeId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping(path = "/{ticketId}/qr-codes")
  public ResponseEntity<byte[]> getTicketQrCode(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID ticketId
  ) {
    byte[] qrCodeImage = qrCodeService.getQrCodeImageForUserAndTicket(
        parseUserId(jwt),
        ticketId
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_PNG);
    headers.setContentLength(qrCodeImage.length);

    return ResponseEntity.ok()
        .headers(headers)
        .body(qrCodeImage);
  }

}
