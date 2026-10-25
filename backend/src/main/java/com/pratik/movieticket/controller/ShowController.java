package com.pratik.movieticket.controller;

import com.pratik.movieticket.dto.SeatLayoutResponse;
import com.pratik.movieticket.dto.ShowRequest;
import com.pratik.movieticket.entity.Show;
import com.pratik.movieticket.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/{id}")
    public Show getShow(@PathVariable Long id) {
        return showService.getShow(id);
    }

    @GetMapping("/{id}/seats")
    public SeatLayoutResponse getSeats(@PathVariable Long id) {
        return showService.getSeatLayout(id);
    }

    @PostMapping
    public ResponseEntity<Show> create(@Valid @RequestBody ShowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.createShow(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
}
