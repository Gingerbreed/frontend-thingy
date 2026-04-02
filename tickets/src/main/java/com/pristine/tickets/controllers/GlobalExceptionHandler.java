package com.pristine.tickets.controllers;

import com.pristine.tickets.domain.dtos.ErrorDto;
import com.pristine.tickets.exceptions.EventNotFoundException;
import com.pristine.tickets.exceptions.EventUpdateException;
import com.pristine.tickets.exceptions.TicketTypeNotFoundException;
import com.pristine.tickets.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDto> handleUserNotFoundExeception(UserNotFoundException ex) {
    log.error("Caught UserNotFoundException", ex);
    String message = String.format("User id does not exist");
    ErrorDto error = new ErrorDto(message);
    return new ResponseEntity<ErrorDto>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
    log.error("Caught ConstraintViolationException", ex);
    ErrorDto dto = new ErrorDto();
    String beep = ex.getConstraintViolations()
      .stream()
      .findFirst()
      .map(violation -> violation.getPropertyPath() + ":"
        + violation.getMessage()).orElse("Constraint violation occurred.");
    dto.setError(beep);
    return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    log.error("Caught MethodArgumentNotValidException", ex);
    String errorMessage = ex.getBindingResult().getFieldErrors().stream().
      findFirst()
      .map(DefaultMessageSourceResolvable::getDefaultMessage)
      .orElse("Validation Failed");
    ErrorDto dto = new ErrorDto(errorMessage);
    return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleValidationException(Exception ex) {
    log.error("Caught exception", ex);
    ErrorDto dto = new ErrorDto();
    dto.setError("An unknown error occurred.");
    return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(EventUpdateException.class)
  public ResponseEntity<ErrorDto> handleEventUpdateException(EventUpdateException ex) {
    log.error("Caught EventUpdateException", ex);
    ErrorDto errorDto = new ErrorDto();
    errorDto.setError("Unable to update event");
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TicketTypeNotFoundException.class)
  public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(TicketTypeNotFoundException ex) {
    log.error("Caught TicketTypeNotFoundException", ex);
    ErrorDto errorDto = new ErrorDto();
    errorDto.setError("Ticket type not found");
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException ex) {
    log.error("Caught EventNotFoundException", ex);
    ErrorDto errorDto = new ErrorDto();
    errorDto.setError("Event not found");
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

}
