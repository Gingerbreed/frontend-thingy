package com.pristine.tickets.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketTypeRequestDto {
  @NotNull(message = "Ticket type id is required.")
  private UUID id;

  @NotBlank(message = "Ticket type name is required.")
  private String name;

  @NotNull(message = "Price is required.")
  @PositiveOrZero(message = "Price must be 0 or greater.")
  private double price;

  private String description;

  private int totalAvailable;
}
