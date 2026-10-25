package com.prachi.movieticket.service;

import com.prachi.movieticket.dto.BookingRequest;
import com.prachi.movieticket.entity.Booking;
import com.prachi.movieticket.entity.BookingStatus;
import com.prachi.movieticket.entity.Show;
import com.prachi.movieticket.exception.BadRequestException;
import com.prachi.movieticket.exception.ResourceNotFoundException;
import com.prachi.movieticket.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowService showService;

    public BookingService(BookingRepository bookingRepository, ShowService showService) {
        this.bookingRepository = bookingRepository;
        this.showService = showService;
    }

    /**
     * Books seats for a show. Synchronized so two requests in this instance
     * cannot reserve the same seat at the same time.
     */
    @Transactional
    public synchronized Booking createBooking(BookingRequest request) {
        Show show = showService.getShow(request.showId());

        if (show.getShowTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("This show has already started");
        }

        Set<String> requested = new LinkedHashSet<>();
        for (String seat : request.seats()) {
            String normalized = seat.trim().toUpperCase();
            if (!show.isValidSeat(normalized)) {
                throw new BadRequestException("Invalid seat: " + seat);
            }
            requested.add(normalized);
        }

        Set<String> alreadyBooked = showService.getBookedSeats(show.getId());
        List<String> conflicts = requested.stream().filter(alreadyBooked::contains).toList();
        if (!conflicts.isEmpty()) {
            throw new BadRequestException("Seats already booked: " + String.join(", ", conflicts));
        }

        Booking booking = new Booking();
        booking.setShow(show);
        booking.setCustomerName(request.customerName().trim());
        booking.setCustomerEmail(request.customerEmail().trim().toLowerCase());
        booking.setPhone(request.phone());
        booking.setSeatNumbers(String.join(",", requested));
        booking.setNumberOfSeats(requested.size());
        booking.setTotalAmount(requested.size() * show.getTicketPrice());
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByCustomerEmailIgnoreCaseOrderByBookedAtDesc(email.trim());
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional
    public Booking cancelBooking(Long id) {
        Booking booking = getBooking(id);
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }
        if (booking.getShow().getShowTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot cancel a booking for a show that has started");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
}
