package com.university.eventmanagement.repository;
import com.university.eventmanagement.model.Event;
import com.university.eventmanagement.model.Rating;
import com.university.eventmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    boolean existByUserAndEvent(User user, Event event);
    Optional<Rating> findByUserAndEvent(User user,Event event);
    List<Rating> findByEvent(Event event);
    List<Rating> findByUser(User user);
}
