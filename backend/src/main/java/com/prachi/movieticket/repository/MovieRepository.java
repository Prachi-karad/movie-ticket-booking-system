package com.prachi.movieticket.repository;

import com.prachi.movieticket.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByGenreIgnoreCase(String genre);

    List<Movie> findByLanguageIgnoreCase(String language);
}
