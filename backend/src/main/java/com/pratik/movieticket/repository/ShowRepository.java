package com.pratik.movieticket.repository;

import com.pratik.movieticket.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieIdAndShowTimeAfterOrderByShowTimeAsc(Long movieId, LocalDateTime after);

    List<Show> findByMovieIdOrderByShowTimeAsc(Long movieId);

    boolean existsByMovieId(Long movieId);
}
