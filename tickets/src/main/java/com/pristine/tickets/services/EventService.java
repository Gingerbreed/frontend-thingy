package com.pristine.tickets.services;

import com.pristine.tickets.domain.CreateEventRequest;
import com.pristine.tickets.domain.entities.Event;

import java.util.UUID;

public interface EventService {
  Event createEvent(UUID organizerId, CreateEventRequest event);
}
