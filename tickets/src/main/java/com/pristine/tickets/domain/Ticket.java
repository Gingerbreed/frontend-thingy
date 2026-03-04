package com.pristine.tickets.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private TicketStatusEnum status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_type_id")
  private TicketType ticketType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "purchaser_id")
  private User purchaser;

  //TODO: Validation

  //TODO: QR Code Hash

  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

}
