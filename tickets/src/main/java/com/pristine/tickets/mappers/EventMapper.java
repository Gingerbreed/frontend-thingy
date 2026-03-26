package com.pristine.tickets.mappers;

import com.pristine.tickets.domain.CreateEventRequest;
import com.pristine.tickets.domain.CreateTicketTypeRequest;
import com.pristine.tickets.domain.UpdateEventRequest;
import com.pristine.tickets.domain.UpdateTicketTypeRequest;
import com.pristine.tickets.domain.dtos.*;
import com.pristine.tickets.domain.entities.Event;
import com.pristine.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel =  "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);
    CreateEventRequest fromDto(CreateEventRequestDto dto);
    CreateEventResponseDto toDto(Event event);
    ListEventTicketTypeResponseDto toDto(TicketType ticketType);
    GetEventTicketTypesResponseDto toGetEventTicketTypesResponseDto(TicketType ticketType);
    ListEventResponseDto toListEventResponseDto(Event event);
    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);
    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);
    UpdateEventRequest fromDto(UpdateEventRequestDto dto);
    UpdateEventResponseDto toUpdateEventResponseDto(Event event);
    UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);
}
