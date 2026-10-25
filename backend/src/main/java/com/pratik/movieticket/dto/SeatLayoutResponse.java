package com.pratik.movieticket.dto;

import java.util.Set;

/** Seat map for a show: layout dimensions plus seats that are already taken. */
public record SeatLayoutResponse(
        Long showId,
        int totalRows,
        int seatsPerRow,
        double ticketPrice,
        Set<String> bookedSeats,
        int availableSeats
) {
}
