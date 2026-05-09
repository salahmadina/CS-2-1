package com.university.eventmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.eventmanagement.model.Event;
import com.university.eventmanagement.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getActiveEvents() {
        return eventRepository.findByStatusInOrderByEventDateAsc(
            List.of(Event.Status.UPCOMING, Event.Status.POSTPONED)
        );
    }

    public List<Event> getPastEvents() {
        return eventRepository.findByStatus(Event.Status.PAST);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        event.setBookedSeats(0);
        event.setStatus(Event.Status.UPCOMING);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedData) {
        Event existing = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));

        boolean dateChanged = !existing.getEventDate().equals(updatedData.getEventDate());

        existing.setName(updatedData.getName());
        existing.setDescription(updatedData.getDescription());
        existing.setEventDate(updatedData.getEventDate());
        existing.setPrice(updatedData.getPrice());
        existing.setTotalSeats(updatedData.getTotalSeats());
        existing.setLocation(updatedData.getLocation());

        if (dateChanged && existing.getStatus() == Event.Status.UPCOMING) {
            existing.setStatus(Event.Status.POSTPONED);
        }

        return eventRepository.save(existing);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public void markAsPast(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(Event.Status.PAST);
        eventRepository.save(event);
    }

    public double getAverageRating(Long eventId) {
        Double avg = eventRepository.findAverageRatingByEventId(eventId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
    public long getRatingCount(Long eventId) {
        return eventRepository.countRatingsByEventId(eventId);
    }
}