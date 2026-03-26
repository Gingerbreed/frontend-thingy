package com.pristine.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketTypeResponseDto {
  private String name;
  private double price;
  private String description;
  private int totalAvailable;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
