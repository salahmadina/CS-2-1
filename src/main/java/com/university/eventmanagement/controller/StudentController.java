package com.university.eventmanagement.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.university.eventmanagement.model.Booking;
import com.university.eventmanagement.model.Event;
import com.university.eventmanagement.model.User;
import com.university.eventmanagement.service.BookingService;
import com.university.eventmanagement.service.EventService;
import com.university.eventmanagement.service.RatingService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student") // acts like a waiter between the user and the service layer, it takes the request, calls the service, and returns a view (api)
public class StudentController {

    @Autowired
    private EventService eventService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RatingService ratingService;

    @GetMapping("/dashboard") 
    public String dashboard(HttpSession session, Model model) {

        User user = getLoggedInStudent(session); //session is memory
        if (user == null) return "redirect:/login";

        List<Event> events = eventService.getActiveEvents();

        Map<Long, Boolean> bookedMap = new HashMap<>();
        Map<Long, Boolean> ratedMap  = new HashMap<>();
        Map<Long, Boolean> canRateMap = new HashMap<>();
        Map<Long, Double> averageRatingMap = new HashMap<>();
        Map<Long, Long> ratingCountMap = new HashMap<>();

        for (Event event : events) {
            bookedMap.put(event.getId(), bookingService.hasUserBookedEvent(user, event));
            ratedMap.put(event.getId(),  ratingService.hasUserRatedEvent(user, event));
            canRateMap.put(event.getId(), event.getEventDate().isBefore(java.time.LocalDateTime.now())
                || event.getStatus() == Event.Status.PAST);
            averageRatingMap.put(event.getId(), eventService.getAverageRating(event.getId()));
            ratingCountMap.put(event.getId(), eventService.getRatingCount(event.getId()));
        }

        model.addAttribute("user", user);
        model.addAttribute("events", events);
        model.addAttribute("bookedMap", bookedMap);
        model.addAttribute("ratedMap", ratedMap);
        model.addAttribute("canRateMap", canRateMap);
        model.addAttribute("averageRatingMap", averageRatingMap);
        model.addAttribute("ratingCountMap", ratingCountMap);

        return "student/dashboard";
    }

    @PostMapping("/book/{eventId}") // add to the db  btb3t 7aga lel db  
    public String bookEvent(@PathVariable Long eventId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        User user = getLoggedInStudent(session);
        if (user == null) return "redirect:/login";

        String result = bookingService.bookEvent(user, eventId);

        if (!result.equals("SUCCESS")) {
            redirectAttributes.addFlashAttribute("error", result);
        } else {
            redirectAttributes.addFlashAttribute("success", "Booked successfully");
        }
        return "redirect:/student/dashboard";
    }

    @GetMapping("/my-bookings")
    public String myBookings(HttpSession session, Model model) {

        User user = getLoggedInStudent(session);
        if (user == null) return "redirect:/login";

        List<Booking> bookings = bookingService.getBookingsForUser(user);

        Map<Long, Boolean> ratedMap = new HashMap<>();
        Map<Long, Boolean> canRateMap = new HashMap<>();
        for (Booking booking : bookings) {
            ratedMap.put(booking.getEvent().getId(),
                         ratingService.hasUserRatedBooking(booking));
            canRateMap.put(booking.getEvent().getId(),
                           ratingService.canRateBooking(booking));
        }

        model.addAttribute("user", user);
        model.addAttribute("bookings", bookings);
        model.addAttribute("ratedMap", ratedMap);
        model.addAttribute("canRateMap", canRateMap);

        return "student/my-bookings";
    }

    @PostMapping("/rate/{eventId}") 
    public String rateEvent(@PathVariable Long eventId,
                            @RequestParam int stars,
                            @RequestParam(required = false) String comment,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        User user = getLoggedInStudent(session);
        if (user == null) return "redirect:/login";

        String result = ratingService.rateEvent(user, eventId, stars, comment);

        if (!result.equals("SUCCESS")) {
            redirectAttributes.addFlashAttribute("error", result);
        } else {
            redirectAttributes.addFlashAttribute("success", "Thank you for your rating!");
        }

        return "redirect:/student/my-bookings";
    }

    private User getLoggedInStudent(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.STUDENT) return null;
        return user;
    }
}
