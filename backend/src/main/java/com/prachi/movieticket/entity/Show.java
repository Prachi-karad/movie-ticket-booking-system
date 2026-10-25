package com.prachi.movieticket.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** A screening of a movie at a theater at a specific time. */
@Entity
@Table(name = "shows") // "show" is a reserved word in MySQL
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(nullable = false)
    private String theaterName;

    private String screen;

    @Column(nullable = false)
    private LocalDateTime showTime;

    private double ticketPrice;

    /** Seat layout: rows are lettered A, B, C... and seats numbered 1..seatsPerRow */
    private int totalRows;

    private int seatsPerRow;

    public Show() {
    }

    public Show(Movie movie, String theaterName, String screen, LocalDateTime showTime,
                double ticketPrice, int totalRows, int seatsPerRow) {
        this.movie = movie;
        this.theaterName = theaterName;
        this.screen = screen;
        this.showTime = showTime;
        this.ticketPrice = ticketPrice;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
    }

    public int getTotalSeats() {
        return totalRows * seatsPerRow;
    }

    /** Returns true if a seat label such as "C7" exists in this show's layout. */
    public boolean isValidSeat(String seat) {
        if (seat == null || seat.length() < 2) return false;
        char row = Character.toUpperCase(seat.charAt(0));
        int rowIndex = row - 'A';
        try {
            int number = Integer.parseInt(seat.substring(1));
            return rowIndex >= 0 && rowIndex < totalRows && number >= 1 && number <= seatsPerRow;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }
    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }
    public String getScreen() { return screen; }
    public void setScreen(String screen) { this.screen = screen; }
    public LocalDateTime getShowTime() { return showTime; }
    public void setShowTime(LocalDateTime showTime) { this.showTime = showTime; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }
    public int getSeatsPerRow() { return seatsPerRow; }
    public void setSeatsPerRow(int seatsPerRow) { this.seatsPerRow = seatsPerRow; }
}
