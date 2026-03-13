package com.pristine.tickets.mappers;

import com.pristine.tickets.domain.CreateEventRequest;
import com.pristine.tickets.domain.CreateTicketTypeRequest;
import com.pristine.tickets.domain.dtos.CreateEventReponseDto;
import com.pristine.tickets.domain.dtos.CreateEventRequestDto;
import com.pristine.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.pristine.tickets.domain.dtos.CreateTicketTypeResponseDto;
import com.pristine.tickets.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel =  "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);
    CreateEventRequest fromDto(CreateEventRequestDto dto);
    CreateEventReponseDto toDto(Event event);
}
