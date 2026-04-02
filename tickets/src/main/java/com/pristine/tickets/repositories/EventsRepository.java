package com.pristine.tickets.repositories;

import com.pristine.tickets.domain.entities.Event;
import com.pristine.tickets.domain.entities.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Event, UUID> {
  Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

  Page<Event> findByStatus(EventStatusEnum status, Pageable pageable);

  Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

  @Query(value = "SELECT * FROM events WHERE " +
    "status = 'PUBLISHED' AND " +
    "to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
    "@@ plainto_tsquery('english', :searchTerm)",
    countQuery = "SELECT count(*) FROM events WHERE " +
      "status = 'PUBLISHED' AND " +
      "to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
      "@@ plainto_tsquery('english', :searchTerm)",
    nativeQuery = true)
  Page<Event> searchEvents(@Param("searchTerm") String searchTerm, Pageable pageable);
}
