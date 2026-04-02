package com.pristine.tickets.controllers;

import com.pristine.tickets.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.pristine.tickets.domain.dtos.ListPublishedEventsResponseDto;
import com.pristine.tickets.domain.entities.Event;
import com.pristine.tickets.mappers.EventMapper;
import com.pristine.tickets.services.EventService;
import io.swagger.v3.core.util.ReferenceTypeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventsController {

  private final EventMapper eventMapper;
  private final EventService eventService;

  @GetMapping
  public ResponseEntity<Page<ListPublishedEventsResponseDto>> listPublishedEvents(
    @RequestParam(required = false) String q,
    Pageable pageable) {
    Page<Event> events;
    if (null != q && !q.trim().isEmpty()) {
      events = eventService.searchPublishedEvents(q, pageable);
    } else {
      events = eventService.listPublishedEvents(pageable);
    }
    return ResponseEntity.ok(events.map(eventMapper::toListPublishedEventsResponseDto));
  }

  @GetMapping(path = "/{eventId}")
  public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(
    @PathVariable UUID eventId
  ) {
    return eventService.getPublishedEvent(eventId)
      .map(eventMapper::toGetPublishedEventDetailsResponseDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }
}
