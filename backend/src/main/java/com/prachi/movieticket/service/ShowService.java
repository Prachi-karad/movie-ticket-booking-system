package com.prachi.movieticket.service;

import com.prachi.movieticket.dto.SeatLayoutResponse;
import com.prachi.movieticket.dto.ShowRequest;
import com.prachi.movieticket.entity.Booking;
import com.prachi.movieticket.entity.BookingStatus;
import com.prachi.movieticket.entity.Movie;
import com.prachi.movieticket.entity.Show;
import com.prachi.movieticket.exception.BadRequestException;
import com.prachi.movieticket.exception.ResourceNotFoundException;
import com.prachi.movieticket.repository.BookingRepository;
import com.prachi.movieticket.repository.ShowRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final MovieService movieService;

    public ShowService(ShowRepository showRepository, BookingRepository bookingRepository, MovieService movieService) {
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.movieService = movieService;
    }

    /** Upcoming shows for a movie (past shows are hidden). */
    public List<Show> getUpcomingShows(Long movieId) {
        movieService.getMovie(movieId);
        return showRepository.findByMovieIdAndShowTimeAfterOrderByShowTimeAsc(movieId, LocalDateTime.now());
    }

    public Show getShow(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id " + id));
    }

    public Show createShow(ShowRequest request) {
        Movie movie = movieService.getMovie(request.movieId());
        Show show = new Show(movie, request.theaterName(), request.screen(), request.showTime(),
                request.ticketPrice(), request.totalRows(), request.seatsPerRow());
        return showRepository.save(show);
    }

    public void deleteShow(Long id) {
        Show show = getShow(id);
        if (bookingRepository.existsByShowId(id)) {
            throw new BadRequestException("Cannot delete a show that already has bookings");
        }
        showRepository.delete(show);
    }

    public Set<String> getBookedSeats(Long showId) {
        Set<String> booked = new TreeSet<>();
        for (Booking b : bookingRepository.findByShowIdAndStatus(showId, BookingStatus.CONFIRMED)) {
            booked.addAll(b.seatList());
        }
        return booked;
    }

    public SeatLayoutResponse getSeatLayout(Long showId) {
        Show show = getShow(showId);
        Set<String> booked = getBookedSeats(showId);
        return new SeatLayoutResponse(show.getId(), show.getTotalRows(), show.getSeatsPerRow(),
                show.getTicketPrice(), booked, show.getTotalSeats() - booked.size());
    }
}
