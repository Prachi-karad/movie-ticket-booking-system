package com.prachi.movieticket.controller;

import com.prachi.movieticket.dto.BookingRequest;
import com.prachi.movieticket.entity.Booking;
import com.prachi.movieticket.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> book(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    /** GET /api/bookings?email=... returns a customer's bookings; without email returns all (admin). */
    @GetMapping
    public List<Booking> getBookings(@RequestParam(required = false) String email) {
        if (email != null && !email.isBlank()) {
            return bookingService.getBookingsByEmail(email);
        }
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @PutMapping("/{id}/cancel")
    public Booking cancel(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }
}
