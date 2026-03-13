package com.pristine.tickets.controllers;

import com.pristine.tickets.domain.CreateEventRequest;
import com.pristine.tickets.domain.dtos.CreateEventReponseDto;
import com.pristine.tickets.domain.dtos.CreateEventRequestDto;
import com.pristine.tickets.domain.entities.Event;
import com.pristine.tickets.mappers.EventMapper;
import com.pristine.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(params = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {

      private final EventMapper eventMapper;
      private final EventService eventService;

      @PostMapping
      public ResponseEntity<CreateEventReponseDto> createEvent(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CreateEventRequestDto createEventRequestDto
        ){
          CreateEventRequest createEventRequest =  eventMapper.fromDto(createEventRequestDto);
          UUID userId = UUID.fromString(jwt.getSubject());
          Event createdEvent = eventService.createEvent(userId, createEventRequest);
          CreateEventReponseDto dto = eventMapper.toDto(createdEvent);
          return  new ResponseEntity<>(dto, HttpStatus.CREATED);
      }
}
