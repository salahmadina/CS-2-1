package com.university.eventmanagement.repository;
import com.university.eventmanagement.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByStatusOrderByEventDateAsc(Event.Status status);
    List<Event> findByStatusInOrderByEventDateAsc(List<Event.Status> statuses);
List<Event> findByStatus(Event.Status status);

@Query("SELECT AVG(r.stars) FROM Rating r Where r.event.id=:eventId")
Double findAverageRatingByEventId(Long eventId);

@Query("SELECT COUNT(r) FROM Rating r WHERE r.event.id=:eventId")
long countRatingsByEventId(Long eventId);
}
