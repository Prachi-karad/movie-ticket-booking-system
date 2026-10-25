package com.pratik.movieticket.repository;

import com.pratik.movieticket.entity.Booking;
import com.pratik.movieticket.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByShowIdAndStatus(Long showId, BookingStatus status);

    List<Booking> findByCustomerEmailIgnoreCaseOrderByBookedAtDesc(String email);

    boolean existsByShowId(Long showId);
}
