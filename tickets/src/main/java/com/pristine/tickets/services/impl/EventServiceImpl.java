package com.pristine.tickets.services.impl;

import com.pristine.tickets.domain.CreateEventRequest;
import com.pristine.tickets.domain.UpdateEventRequest;
import com.pristine.tickets.domain.UpdateTicketTypeRequest;
import com.pristine.tickets.domain.entities.Event;
import com.pristine.tickets.domain.entities.EventStatusEnum;
import com.pristine.tickets.domain.entities.TicketType;
import com.pristine.tickets.domain.entities.User;
import com.pristine.tickets.exceptions.EventNotFoundException;
import com.pristine.tickets.exceptions.EventUpdateException;
import com.pristine.tickets.exceptions.TicketTypeNotFoundException;
import com.pristine.tickets.exceptions.UserNotFoundException;
import com.pristine.tickets.repositories.EventsRepository;
import com.pristine.tickets.repositories.UserRepository;
import com.pristine.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

  @Override
  @Transactional
  public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event) {
    if (null == event.getId()) {
      throw new EventUpdateException("Event ID cannot be null");
    }
    if (!id.equals(event.getId())) {
      throw new EventUpdateException("Cannot update the ID of an event");
    }
    Event existingEvent = eventsRepository
      .findByIdAndOrganizerId(id, organizerId)
      .orElseThrow(() -> new EventNotFoundException(
        String.format("Event with ID '%s' does not exist", id))
      );

    existingEvent.setName(event.getName());
    existingEvent.setStart(event.getStart());
    existingEvent.setEnd(event.getEnd());
    existingEvent.setVenue(event.getVenue());
    existingEvent.setSalesStart(event.getSalesStart());
    existingEvent.setSalesEnd(event.getSalesEnd());
    existingEvent.setStatus(event.getStatus());

    Set<UUID> requestTicketTypeIds = event.getTicketTypes()
      .stream()
      .map(UpdateTicketTypeRequest::getId)
      .filter(Objects::nonNull)
      .collect(Collectors.toSet());

    existingEvent.getTicketTypes().removeIf(existingTicketType ->
      !requestTicketTypeIds.contains(existingTicketType.getId())
    );

    Map<UUID, TicketType> existingTicketTypesIndex = existingEvent.getTicketTypes().stream()
      .collect(Collectors.toMap(TicketType::getId, Function.identity()));

    for (UpdateTicketTypeRequest ticketType : event.getTicketTypes()) {
      if (null == ticketType.getId()) {
        // Create
        TicketType ticketTypeToCreate = new TicketType();
        ticketTypeToCreate.setName(ticketType.getName());
        ticketTypeToCreate.setPrice(ticketType.getPrice());
        ticketTypeToCreate.setDescription(ticketType.getDescription());
        ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
        ticketTypeToCreate.setEvent(existingEvent);
        existingEvent.getTicketTypes().add(ticketTypeToCreate);
      } else if (existingTicketTypesIndex.containsKey(ticketType.getId())) {
        // Update
        TicketType existingTicketType = existingTicketTypesIndex.get(ticketType.getId());
        existingTicketType.setName(ticketType.getName());
        existingTicketType.setPrice(ticketType.getPrice());
        existingTicketType.setDescription(ticketType.getDescription());
        existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
      } else {
        throw new TicketTypeNotFoundException(String.format(
          "Ticket type with ID '%s' does not exist", ticketType.getId()
        ));
      }
    }
    return eventsRepository.save(existingEvent);
  }

  @Override
  @Transactional
  public void deleteEventForOrganizer(UUID organizerId, UUID id) {
    getEventForOrganizer(organizerId, id).ifPresent(eventsRepository::delete);
  }

  @Override
  public Page<Event> listPublishedEvents(Pageable pageable) {
    return eventsRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable);
  }

  @Override
  public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
    return eventsRepository.searchEvents(query, pageable);
  }

}
