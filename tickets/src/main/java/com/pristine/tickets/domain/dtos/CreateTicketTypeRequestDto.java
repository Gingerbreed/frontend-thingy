package com.pristine.tickets.domain.dtos;

import com.pristine.tickets.domain.CreateTicketTypeRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequestDto {
  @NotBlank(message = "Ticket type name is required.")
  private String name;

  @NotNull(message = "Price is required.")
  @PositiveOrZero(message = "Price must be 0 or greater.")
  private double price;

  private String description;

  private int totalAvailable;
}
