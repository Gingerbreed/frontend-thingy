package com.pristine.tickets.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequest {
  private String name;
  private double price;
  private String description;
  private int totalAvailable;
  private List<CreateTicketTypeRequest> ticketTypes = new ArrayList<>();
}
