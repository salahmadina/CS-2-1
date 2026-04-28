package com.university.eventmanagement.service;

import com.university.eventmanagement.model.Event;
import com.university.eventmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    
    @Autowired
    private EventRespoitory eventRepository;

    public List<Event> getActiveEvents(){
        return eventRepository.findByStatusInOderByEventDataAsc(
            List.of(Event.Status.UPCOMING, Event.Status.POSTPONED)
        );
    }
    
    public List<Event> getPastEvents(){
        return eventRepository.findByStatus(Event.Status.PAST);
    }

    public Optional<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id){
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event){
        event.BookedSeats(0);
        event.setStatus(Event.Status.UPCOMING);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updateData){
        Event existing = eventRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Event not found"));

        boolean dateChanged = !existing.getEventDate().equals(updatedData.getEventDate());

        existing.setName(updatedData.getName());
        existing.setDescription(updatedData.getDescription);
        existing.setEventDate(updatedData.getEventDate());
        existing.setPrice(updatedData.getprice());
        existing.setTotalSeats(updatedData.getTotalSeats());
        existing.setLocation(updatedData.getLocation());

        if (dataChanged && existing.getStatus() == Event.Status.UPCOMING){
            existing.setStatus(Event.Status.POSTPONED);
        
            return eventRepository.save(existing);
        }

        public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
        }


        public void markAsPast(Long id){
            Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(Event.Status.PAST);
        eventRepository.save(event);
        }

        public double getAverageRating(Long eventId) {
        Double avg = eventRepository.findAverageRatingByEventId(eventId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
        }

    

}
