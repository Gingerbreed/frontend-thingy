package com.pristine.tickets.services.impl;

import com.pristine.tickets.domain.CreateEventRequest;
import com.pristine.tickets.domain.entities.Event;
import com.pristine.tickets.domain.entities.TicketType;
import com.pristine.tickets.domain.entities.User;
import com.pristine.tickets.exceptions.UserNotFoundException;
import com.pristine.tickets.repositories.EventsRepository;
import com.pristine.tickets.repositories.UserRepository;
import com.pristine.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
  private final UserRepository userRepository;
  private final EventsRepository eventsRepository;

  @Override
  public Event createEvent(UUID organizerId, CreateEventRequest event) {
// Find the organizer or throw an exception if not found
    User organizer = userRepository.findById(organizerId)
      .orElseThrow(() -> new UserNotFoundException(
        String.format("User with ID '%s' not found", organizerId))
      );


    Event eventToCreate = new Event();
// Create ticket types
    List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
      ticketType -> {
        TicketType ticketTypeToCreate = new TicketType();
        ticketTypeToCreate.setName(ticketType.getName());
        ticketTypeToCreate.setPrice(ticketType.getPrice());
        ticketTypeToCreate.setDescription(ticketType.getDescription());
        ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
        ticketTypeToCreate.setEvent(eventToCreate);
        return ticketTypeToCreate;
      }).toList();
// Create and populate the event
    eventToCreate.setName(event.getName());
    eventToCreate.setStart(event.getStart());
    eventToCreate.setEnd(event.getEnd());
    eventToCreate.setVenue(event.getVenue());
    eventToCreate.setSalesStart(event.getSalesStart());
    eventToCreate.setSalesEnd(event.getSalesEnd());
    eventToCreate.setStatus(event.getStatus());
    eventToCreate.setOrganizer(organizer);
    eventToCreate.setTicketTypes(ticketTypesToCreate);
    return eventsRepository.save(eventToCreate);
  }

  @Override
  public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
    User organizer = userRepository.findById(organizerId)
      .orElseThrow(() -> new UserNotFoundException(
        String.format("User with ID '%s' not found", organizerId))
      );
    return eventsRepository.findByOrganizerId(organizerId, pageable);
  }

  @Override
  public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
    return eventsRepository.findByIdAndOrganizerId(id, organizerId);
  }
}
