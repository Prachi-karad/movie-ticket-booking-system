package com.pratik.movieticket.service;

import com.pratik.movieticket.entity.Movie;
import com.pratik.movieticket.exception.BadRequestException;
import com.pratik.movieticket.exception.ResourceNotFoundException;
import com.pratik.movieticket.repository.MovieRepository;
import com.pratik.movieticket.repository.ShowRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;

    public MovieService(MovieRepository movieRepository, ShowRepository showRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
    }

    public List<Movie> getMovies(String search, String genre, String language) {
        if (search != null && !search.isBlank()) {
            return movieRepository.findByTitleContainingIgnoreCase(search.trim());
        }
        if (genre != null && !genre.isBlank()) {
            return movieRepository.findByGenreIgnoreCase(genre.trim());
        }
        if (language != null && !language.isBlank()) {
            return movieRepository.findByLanguageIgnoreCase(language.trim());
        }
        return movieRepository.findAll();
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    public Movie createMovie(Movie movie) {
        movie.setId(null);
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updated) {
        Movie movie = getMovie(id);
        movie.setTitle(updated.getTitle());
        movie.setDescription(updated.getDescription());
        movie.setGenre(updated.getGenre());
        movie.setLanguage(updated.getLanguage());
        movie.setDurationMinutes(updated.getDurationMinutes());
        movie.setCertificate(updated.getCertificate());
        movie.setPosterUrl(updated.getPosterUrl());
        movie.setReleaseDate(updated.getReleaseDate());
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = getMovie(id);
        if (showRepository.existsByMovieId(id)) {
            throw new BadRequestException("Cannot delete a movie that has shows scheduled");
        }
        movieRepository.delete(movie);
    }
}
