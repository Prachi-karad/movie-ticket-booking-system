package com.prachi.movieticket.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record BookingRequest(
        @NotNull(message = "showId is required") Long showId,
        @NotBlank(message = "Name is required") String customerName,
        @NotBlank(message = "Email is required") @Email(message = "Enter a valid email") String customerEmail,
        @Pattern(regexp = "^$|^[0-9]{10}$", message = "Phone must be 10 digits") String phone,
        @NotEmpty(message = "Select at least one seat") @Size(max = 10, message = "You can book up to 10 seats at a time") List<String> seats
) {
}
