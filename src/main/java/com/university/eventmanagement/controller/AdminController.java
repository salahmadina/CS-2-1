package com.university.eventmanagement.controller;

import com.university.eventmanagement.model.Booking;
import com.university.eventmanagement.model.Event;
import com.university.eventmanagement.model.User;
import com.university.eventmanagement.service.BookingService;
import com.university.eventmanagement.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller 
@RequestMapping
public class AdminController {
    
    @Autowired 
    private EventService eventService;

    @Autowired 
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard (HttpSession session, Model model){

        User admin = getLoggedInAdmin(session);
        if (admin == null)
            return "redirect:/login";

        List <Event> allEvents = eventService.getAllEvents();
        List <Booking> allBookings = bookingService.getAllBookings();

        long pendingCount = allBookings.stream().filter(b -> b.getPaymentStatus() == Booking.PaymentStatus.PENDING).count();

        model.addAttribute("admin", admin);
        model.addAttribute("events", allEvents);
        model.addAttribute("totalEvents", allEvents.size());
        model.addAttribute("totalBookings", allBookings.size());
        model.addAttribute("pendingCount", pendingCount);

        return "admin/dashboard";
    }

    


}
