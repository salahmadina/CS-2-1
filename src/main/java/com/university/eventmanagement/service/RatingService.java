package com.university.eventmanagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.eventmanagement.model.Booking;
import com.university.eventmanagement.model.Event;
import com.university.eventmanagement.model.Rating;
import com.university.eventmanagement.model.User;
import com.university.eventmanagement.repository.BookingRepository;
import com.university.eventmanagement.repository.EventRepository;
import com.university.eventmanagement.repository.RatingRepository;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    public String rateEvent(User user, Long eventId, int stars, String comment) {

        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

        Optional<Booking> bookingOpt = bookingRepository.findByUserAndEvent(user, event);
        if (bookingOpt.isEmpty()) {
            return "You can only rate events you have booked.";
        }

        Booking booking = bookingOpt.get();

        if (!canRateBooking(booking)) {
            return "You can rate this event only after it has been completed.";
        }

        if (ratingRepository.existsByUserAndEvent(user, event)) {
            return "You have already rated this event.";
        }

        if (stars < 1 || stars > 5) {
            return "Rating must be between 1 and 5 stars.";
        }

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setEvent(event);
        rating.setStars(stars);
        rating.setComment(comment);

        ratingRepository.save(rating);
        return "SUCCESS";
    }

    public List<Rating> getRatingsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        return ratingRepository.findByEvent(event);
    }

    public boolean hasUserRatedEvent(User user, Event event) {
        return ratingRepository.existsByUserAndEvent(user, event);
    }

    public Optional<Rating> getUserRating(User user, Event event) {
        return ratingRepository.findByUserAndEvent(user, event);
    }

    public boolean hasUserRatedBooking(Booking booking) {
        return ratingRepository.existsByUserAndEvent(booking.getUser(), booking.getEvent());
    }

    public boolean canRateBooking(Booking booking) {
        Event event = booking.getEvent();
        if (event == null || event.getEventDate() == null) {
            return false;
        }

        if (event.isCancelled()) {
            return false;
        }

        return event.getStatus() == Event.Status.PAST
            || !event.getEventDate().isAfter(LocalDateTime.now());
    }
}
