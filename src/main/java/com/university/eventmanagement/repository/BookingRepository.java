package com.university.eventmanagement.repository;
import com.university.eventmanagement.model.Booking;
import com.university.eventmanagement.model.Event;
import com.university.eventmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>{

    List<Booking> findByUser(User user);
    List<Booking> findByEvent(Event event);
    boolean existsByUserAndEvent(User user,Event event);
    Optional<Booking> findByUserAndEvent(User user,Event event);
    List<Booking> findByPaymentStatus(Booking.PaymentStatus paymentStatus);
    List<Booking> findAllByOrderByBookedAtDesc();
    
}
