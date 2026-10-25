package com.pratik.movieticket.controller;

import com.pratik.movieticket.entity.Movie;
import com.pratik.movieticket.entity.Show;
import com.pratik.movieticket.service.MovieService;
import com.pratik.movieticket.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final ShowService showService;

    public MovieController(MovieService movieService, ShowService showService) {
        this.movieService = movieService;
        this.showService = showService;
    }

    @GetMapping
    public List<Movie> getMovies(@RequestParam(required = false) String search,
                                 @RequestParam(required = false) String genre,
                                 @RequestParam(required = false) String language) {
        return movieService.getMovies(search, genre, language);
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    @GetMapping("/{id}/shows")
    public List<Show> getShows(@PathVariable Long id) {
        return showService.getUpcomingShows(id);
    }

    @PostMapping
    public ResponseEntity<Movie> create(@Valid @RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movie));
    }

    @PutMapping("/{id}")
    public Movie update(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
