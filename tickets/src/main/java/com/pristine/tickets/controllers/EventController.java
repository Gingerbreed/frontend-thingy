package com.pristine.tickets.controllers;

import com.pristine.tickets.domain.CreateEventRequest;
import com.pristine.tickets.domain.UpdateEventRequest;
import com.pristine.tickets.domain.dtos.*;
import com.pristine.tickets.domain.entities.Event;
import com.pristine.tickets.mappers.EventMapper;
import com.pristine.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {

      private final EventMapper eventMapper;
      private final EventService eventService;

      @PostMapping
      public ResponseEntity<CreateEventResponseDto> createEvent(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CreateEventRequestDto createEventRequestDto
        ){
          CreateEventRequest createEventRequest =  eventMapper.fromDto(createEventRequestDto);
          Event createdEvent = eventService.createEvent(parseUserId(jwt), createEventRequest);
          CreateEventResponseDto dto = eventMapper.toDto(createdEvent);
          return  new ResponseEntity<>(dto, HttpStatus.CREATED);
      }

      @GetMapping
      public ResponseEntity<Page<ListEventResponseDto>> ListEvent(
        @AuthenticationPrincipal Jwt jwt, Pageable pageable
      ){
          Page<Event> events = eventService.listEventsForOrganizer(parseUserId(jwt), pageable);
          return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));
      }

      @GetMapping(path = "/{eventId}")
      public ResponseEntity<GetEventDetailsResponseDto> GetEvent(
        @AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId
      ){
         return eventService.getEventForOrganizer(parseUserId(jwt), eventId)
           .map(eventMapper::toGetEventDetailsResponseDto)
           .map(ResponseEntity::ok)
           .orElse(ResponseEntity.notFound().build());

      }

  @PutMapping(path = "/{eventId}")
  public ResponseEntity<UpdateEventResponseDto> updateEvent(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID eventId,
    @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto
  ){
    UpdateEventRequest updateEventRequest =  eventMapper.fromDto(updateEventRequestDto);
    Event updatedEvent = eventService.updateEventForOrganizer(parseUserId(jwt), eventId, updateEventRequest);
    UpdateEventResponseDto dto = eventMapper.toUpdateEventResponseDto(updatedEvent);
    return ResponseEntity.ok(dto);
  }

      private UUID parseUserId(Jwt jwt){
        return UUID.fromString(jwt.getSubject());
      }
}
