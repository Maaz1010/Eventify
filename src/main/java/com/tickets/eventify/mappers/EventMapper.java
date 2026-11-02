package com.tickets.eventify.mappers;

import com.tickets.eventify.domain.CreateEventRequest;
import com.tickets.eventify.domain.CreateTicketTypeRequest;
import com.tickets.eventify.domain.UpdateEventRequest;
import com.tickets.eventify.domain.UpdateTicketTypeRequest;
import com.tickets.eventify.domain.dtos.CreateEventRequestDto;
import com.tickets.eventify.domain.dtos.CreateEventResponseDto;
import com.tickets.eventify.domain.dtos.CreateTicketTypeRequestDto;
import com.tickets.eventify.domain.dtos.GetEventDetailsResponseDto;
import com.tickets.eventify.domain.dtos.GetEventDetailsTicketTypesResponseDto;
import com.tickets.eventify.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.tickets.eventify.domain.dtos.GetPublishedEventDetailsTicketTypesResponseDto;
import com.tickets.eventify.domain.dtos.ListEventResponseDto;
import com.tickets.eventify.domain.dtos.ListEventTicketTypeResponseDto;
import com.tickets.eventify.domain.dtos.ListPublishedEventResponseDto;
import com.tickets.eventify.domain.dtos.UpdateEventRequestDto;
import com.tickets.eventify.domain.dtos.UpdateEventResponseDto;
import com.tickets.eventify.domain.dtos.UpdateTicketTypeRequestDto;
import com.tickets.eventify.domain.dtos.UpdateTicketTypeResponseDto;
import com.tickets.eventify.domain.entities.Event;
import com.tickets.eventify.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

  CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

  CreateEventRequest fromDto(CreateEventRequestDto dto);

  CreateEventResponseDto toDto(Event event);

  ListEventTicketTypeResponseDto toDto(TicketType ticketType);

  ListEventResponseDto toListEventResponseDto(Event event);

  GetEventDetailsTicketTypesResponseDto toGetEventDetailsTicketTypesResponseDto(
      TicketType ticketType);

  GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

  UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);

  UpdateEventRequest fromDto(UpdateEventRequestDto dto);

  UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);

  UpdateEventResponseDto toUpdateEventResponseDto(Event event);

  ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);

  GetPublishedEventDetailsTicketTypesResponseDto toGetPublishedEventDetailsTicketTypesResponseDto(
      TicketType ticketType);

  GetPublishedEventDetailsResponseDto toGetPublishedEventDetailsResponseDto(Event event);
}
