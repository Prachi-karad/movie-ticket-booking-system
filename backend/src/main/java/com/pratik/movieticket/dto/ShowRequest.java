package com.pratik.movieticket.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record ShowRequest(
        @NotNull(message = "movieId is required") Long movieId,
        @NotBlank(message = "Theater name is required") String theaterName,
        String screen,
        @NotNull(message = "Show time is required") @Future(message = "Show time must be in the future") LocalDateTime showTime,
        @Positive(message = "Ticket price must be positive") double ticketPrice,
        @Min(1) @Max(26) int totalRows,
        @Min(1) @Max(30) int seatsPerRow
) {
}
