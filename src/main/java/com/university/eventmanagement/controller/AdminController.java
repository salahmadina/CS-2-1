package com.university.eventmanagement.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        List<Event> allEvents     = eventService.getAllEvents();
        List<Booking> allBookings = bookingService.getAllBookings();

        long pendingCount = allBookings.stream()
            .filter(b -> b.getPaymentStatus() == Booking.PaymentStatus.PENDING)
            .count();

        Map<Long, Double> averageRatingMap = new HashMap<>();
        Map<Long, Long> ratingCountMap = new HashMap<>();
        for (Event event : allEvents) {
            averageRatingMap.put(event.getId(), eventService.getAverageRating(event.getId()));
            ratingCountMap.put(event.getId(), eventService.getRatingCount(event.getId()));
        }

        model.addAttribute("admin", admin);
        model.addAttribute("events", allEvents);
        model.addAttribute("totalEvents", allEvents.size());
        model.addAttribute("totalBookings", allBookings.size());
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("averageRatingMap", averageRatingMap);
        model.addAttribute("ratingCountMap", ratingCountMap);

        return "admin/dashboard";
    }

    @GetMapping("/events")
    public String manageEvents(HttpSession session, Model model) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        List<Event> events = eventService.getAllEvents();
        Map<Long, Double> averageRatingMap = new HashMap<>();
        Map<Long, Long> ratingCountMap = new HashMap<>();

        for (Event event : events) {
            averageRatingMap.put(event.getId(), eventService.getAverageRating(event.getId()));
            ratingCountMap.put(event.getId(), eventService.getRatingCount(event.getId()));
        }

        model.addAttribute("admin", admin);
        model.addAttribute("events", events);
        model.addAttribute("averageRatingMap", averageRatingMap);
        model.addAttribute("ratingCountMap", ratingCountMap);

        return "admin/events";
    }

    @GetMapping("/events/new")
    public String showAddEventForm(HttpSession session, Model model) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        model.addAttribute("admin", admin);
        model.addAttribute("event", new Event());

        return "admin/event-form";
    }

    @PostMapping("/events/save")
    public String saveEvent(@RequestParam String name,
                            @RequestParam String description,
                            @RequestParam String eventDate,
                            @RequestParam String price,
                            @RequestParam(required = false) String totalSeats,
                            @RequestParam String location,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setLocation(location);
        event.setEventDate(LocalDateTime.parse(eventDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        event.setPrice(price.isBlank() ? BigDecimal.ZERO : new BigDecimal(price));
        event.setTotalSeats(totalSeats == null || totalSeats.isBlank()
            ? null : Integer.parseInt(totalSeats));

        eventService.createEvent(event);
        redirectAttributes.addFlashAttribute("success", "Event created successfully!");
        return "redirect:/admin/events";
    }

    @GetMapping("/events/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               HttpSession session,
                               Model model) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        Optional<Event> eventOpt = eventService.findById(id);
        if (eventOpt.isEmpty()) return "redirect:/admin/events";

        model.addAttribute("admin", admin);
        model.addAttribute("event", eventOpt.get());

        return "admin/event-form";
    }

    @PostMapping("/events/update/{id}")
    public String updateEvent(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam String eventDate,
                              @RequestParam String price,
                              @RequestParam(required = false) String totalSeats,
                              @RequestParam String location,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        Event updatedData = new Event();
        updatedData.setName(name);
        updatedData.setDescription(description);
        updatedData.setLocation(location);
        updatedData.setEventDate(LocalDateTime.parse(eventDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        updatedData.setPrice(price.isBlank() ? BigDecimal.ZERO : new BigDecimal(price));
        updatedData.setTotalSeats(totalSeats == null || totalSeats.isBlank()
            ? null : Integer.parseInt(totalSeats));

        eventService.updateEvent(id, updatedData);
        redirectAttributes.addFlashAttribute("success", "Event updated successfully!");
        return "redirect:/admin/events";
    }

    @PostMapping("/events/cancel/{id}")
    public String cancelEvent(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("success", "Event cancelled. Existing bookings and payments were preserved.");
        return "redirect:/admin/events";
    }

    @PostMapping("/events/past/{id}")
    public String markAsPast(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        eventService.markAsPast(id);
        redirectAttributes.addFlashAttribute("success", "Event moved to Past.");
        return "redirect:/admin/events";
    }

    @GetMapping("/bookings")
    public String manageBookings(HttpSession session, Model model) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        model.addAttribute("admin", admin);
        model.addAttribute("bookings", bookingService.getAllBookings());

        return "admin/bookings";
    }

    @PostMapping("/bookings/confirm/{bookingId}")
    public String confirmPayment(@PathVariable Long bookingId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        String result = bookingService.confirmPayment(bookingId);

        if (!result.equals("SUCCESS")) {
            redirectAttributes.addFlashAttribute("error", result);
        } else {
            redirectAttributes.addFlashAttribute("success", "Payment confirmed!");
        }

        return "redirect:/admin/bookings";
    }

    @PostMapping("/bookings/refund/{bookingId}")
    public String confirmRefund(@PathVariable Long bookingId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        User admin = getLoggedInAdmin(session);
        if (admin == null) return "redirect:/login";

        String result = bookingService.confirmRefund(bookingId);

        if (!result.equals("SUCCESS")) {
            redirectAttributes.addFlashAttribute("error", result);
        } else {
            redirectAttributes.addFlashAttribute("success", "Refund confirmed for student.");
        }

        return "redirect:/admin/bookings";
    }

    private User getLoggedInAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != User.Role.ADMIN) return null;
        return user;
    }
}